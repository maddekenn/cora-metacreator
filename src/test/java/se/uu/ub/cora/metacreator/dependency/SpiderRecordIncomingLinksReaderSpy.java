package se.uu.ub.cora.metacreator.dependency;

import se.uu.ub.cora.spider.data.SpiderDataList;
import se.uu.ub.cora.spider.data.SpiderDataRecord;
import se.uu.ub.cora.spider.record.SpiderRecordIncomingLinksReader;
import se.uu.ub.cora.storage.RecordNotFoundException;

public class SpiderRecordIncomingLinksReaderSpy implements SpiderRecordIncomingLinksReader {
	public SpiderDataRecord record;

	@Override
	public SpiderDataList readIncomingLinks(String userId, String type, String id) {
		if (null == record) {
			throw new RecordNotFoundException("Record not found");
		}
		return null;
	}
}
