package se.uu.ub.cora.metacreator.dependency;

import java.util.Collection;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageSpy implements RecordStorage {

	@Override
	public DataGroup read(String type, String id) {
		if ("countryCollectionItem".equals(id) || "genericCollectionItem".equals(id)) {
			DataGroup dataGroup = DataGroup.withNameInData("recordType");
			DataGroup parentGroup = DataGroup.withNameInData("parentId");
			parentGroup.addChild(
					DataAtomic.withNameInDataAndValue("" + "linkedRecordType", "recordType"));
			parentGroup.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordId", "metadataCollectionItem"));
			dataGroup.addChild(parentGroup);
			return dataGroup;
		}
		return DataGroup.withNameInData("recordType");
	}

	@Override
	public void create(String type, String id, DataGroup record, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByTypeAndId(String type, String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean linksExistForRecord(String type, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup record, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<DataGroup> readList(String type, DataGroup filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> readAbstractList(String type, DataGroup filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGroup readLinkList(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> generateLinkCollectionPointingToRecord(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean recordsExistForRecordType(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String type,
			String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
