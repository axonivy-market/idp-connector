package com.axonivy.connector.idp.demo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;

import com.axonivy.connector.idp.connector.model.GenericSplittingProcessingCompleted;
import com.axonivy.connector.idp.connector.model.StringExtraction;
import com.axonivy.connector.idp.connector.model.SubDocument1;
import com.axonivy.connector.idp.demo.dto.Extraction;
import com.axonivy.connector.idp.demo.dto.SubDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ivyteam.ivy.environment.Ivy;
import static com.axonivy.connector.idp.connector.utils.Constants.EXTRACTIONS;

public class IDPDemoUtils {
	private static final String NAME = "name";
	private static final String VALUE = "value";
	private static final String LINEITEM = "line_item";
	private static final String LINEITEMS = "line_items";

	public static List<SubDocument> toSubDocumentList(JsonNode json)
			throws JsonProcessingException, IllegalArgumentException {
		if (!json.has("document_splitting")) {
			throw new IllegalArgumentException("No document_splitting in response.");
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
		mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

		GenericSplittingProcessingCompleted processingCompleted = mapper.treeToValue(json,
				GenericSplittingProcessingCompleted.class);
		return toSubDocumentList(processingCompleted);
	}

	public static List<SubDocument> toSubDocumentList(GenericSplittingProcessingCompleted result) {
		if (result == null || result.getDocumentSplitting() == null
				|| CollectionUtils.isEmpty(result.getDocumentSplitting().getSubDocuments())) {
			return null;
		}

		List<SubDocument> subDocuments = new ArrayList<SubDocument>();
		for (int i = 0; i < result.getDocumentSplitting().getSubDocuments().size(); i++) {
			SubDocument1 subDoc1 = result.getDocumentSplitting().getSubDocuments().get(i);
			subDocuments.add(new SubDocument(subDoc1.getName(), subDoc1.getPages(), null));
		}
		for (int i = 0; i < result.getSubPdfs().size(); i++) {
			subDocuments.get(i).setSubPdf(result.getSubPdfs().get(i));
		}
		return subDocuments;
	}

	public static List<SubDocument> fromSubDocumentAndPDF(List<SubDocument1> subDocList, List<String> subPdfs) {
		List<SubDocument> subDocuments = new ArrayList<SubDocument>();
		for (int i = 0; i < subPdfs.size(); i++) {
			subDocuments
					.add(new SubDocument(subDocList.get(i).getName(), subDocList.get(i).getPages(), subPdfs.get(i)));
		}
		return subDocuments;
	}

	public static List<Extraction> toExactionList(JsonNode jsonNode)
			throws JsonProcessingException, IllegalArgumentException {
		JsonNode extractionNode = jsonNode;
		if (jsonNode.has(EXTRACTIONS)) {
			extractionNode = jsonNode.get(EXTRACTIONS);
		}
		if (!extractionNode.has("document_type")) {
			throw new IllegalArgumentException("No document_type in json.");
		}
		List<Extraction> extractions = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
		mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

		for (Entry<String, JsonNode> field : extractionNode.properties()) {
			if (field.getValue() == null) {
				continue;
			}
			JsonNode stringExtractionNode = field.getValue();
			if (field.getValue().has(NAME)) {
				stringExtractionNode = field.getValue().get(NAME);
			}
			if (stringExtractionNode.has(VALUE)) {
				StringExtraction stringExtraction = mapper.treeToValue(stringExtractionNode, StringExtraction.class);
				extractions.add(new Extraction(field.getKey(), stringExtraction));
			}
		}
		return extractions;
	}

	/**
	 * get list header title
	 * 
	 * @param jsonNode
	 * @param onlyNoNull : get the header which all value is not null
	 * @return
	 * @throws JsonProcessingException
	 * @throws IllegalArgumentException
	 */
	public static List<String> extractLineItemHeader(JsonNode jsonNode, boolean onlyNoNull)
			throws JsonProcessingException, IllegalArgumentException {
		String lineitemKey = getLineItemKey(jsonNode);

		if (lineitemKey == null) {
			Ivy.log().warn("No line_item or line_items in json.");
			return new ArrayList<String>();
		}

		List<String> header = new ArrayList<>();
		if (!jsonNode.get(lineitemKey).isArray() && !jsonNode.get(lineitemKey).isEmpty()) {
			return header;
		}
		JsonNode lineItem = jsonNode.get(lineitemKey).get(0);
		Iterator<String> fieldNameIterator = lineItem.fieldNames();
		while (fieldNameIterator.hasNext()) {
			String fieldName = fieldNameIterator.next();
			if (!onlyNoNull || lineItem.get(fieldName).has(VALUE)) {
				header.add(fieldName);
			}
		}
		return header;
	}

	private static String getLineItemKey(JsonNode jsonNode) {
		String lineitemKey = null;
		if (jsonNode.has(LINEITEM)) {
			lineitemKey = LINEITEM;
		}
		if (lineitemKey == null && jsonNode.has(LINEITEMS)) {
			lineitemKey = LINEITEMS;
		}
		return lineitemKey;
	}

	public static List<Map<String, String>> extractLineItems(JsonNode jsonNode) throws JsonProcessingException {
		String lineitemKey = getLineItemKey(jsonNode);

		if (lineitemKey == null) {
			Ivy.log().warn("No line_item or line_items in json.");
			return null;
		}
		List<Map<String, String>> lineitems = new ArrayList<>();
		for (JsonNode itemNode : jsonNode.get(lineitemKey)) {
			Map<String, String> map = new HashMap<>();
			for (Entry<String, JsonNode> field : itemNode.properties()) {
				if (field.getValue() != null && field.getValue().has(VALUE)) {
					map.put(field.getKey(), field.getValue().get(VALUE).asText());
				}
			}
			lineitems.add(map);
		}
		return lineitems;
	}
}
