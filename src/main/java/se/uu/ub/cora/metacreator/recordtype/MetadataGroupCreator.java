package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataGroupCreator {


	private String id;
	private String dataDivider;

	private MetadataGroupCreator(String id, String dataDivider) {
		this.id = id;
		this.dataDivider = dataDivider;
	}
	public static MetadataGroupCreator withIdAndNameInData(String id, String dataDivider) {
		return new MetadataGroupCreator(id, dataDivider);
	}
	public SpiderDataGroup createMetadataGroup(String refRecordInfoId) {
		SpiderDataGroup metadataGroup = SpiderDataGroup.withNameInData("metadata");	
		
		createAndAddRecordInfo(metadataGroup);
		//TODO: maybe nameInData should be provided
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", id));
		addTextIds(metadataGroup);
		
		addChildReferences(metadataGroup, refRecordInfoId);
		
		addAttributeType(metadataGroup);
		
		return metadataGroup;
	}

	private void createAndAddRecordInfo(SpiderDataGroup metadataGroup) {
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));

		SpiderDataGroup dataDividerGroup = SpiderDataGroup.withNameInData("dataDivider");
		dataDividerGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDividerGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", dataDivider));

		recordInfo.addChild(dataDividerGroup);
		
		metadataGroup.addChild(recordInfo);
	}
	
	private void addTextIds(SpiderDataGroup metadataGroup) {
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", id+"Text"));
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", id+"DefText"));
	}
	
	private void addChildReferences(SpiderDataGroup metadataGroup, String refRecordInfoId) {
		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");
		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("ref", refRecordInfoId));
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMin", "1"));
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMax", "1"));
		childReference.setRepeatId("0");
		childReferences.addChild(childReference);
		metadataGroup.addChild(childReferences);
	}
	private void addAttributeType(SpiderDataGroup metadataGroup) {
		metadataGroup.addAttributeByIdWithValue("type", "group");
	}
	
}
