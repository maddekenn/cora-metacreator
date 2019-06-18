package se.uu.ub.cora.metacreator.dependency;

import se.uu.ub.cora.data.DataList;
import se.uu.ub.cora.spider.data.SpiderDataRecord;
import se.uu.ub.cora.spider.record.SpiderRecordIncomingLinksReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class SpiderRecordIncomingLinksReaderSpy implements SpiderRecordIncomingLinksReader {
	public SpiderDataRecord record;

	@Override
	public DataList readIncomingLinks(String userId, String type, String id) {
		if (null == record) {
			throw new RecordNotFoundException("Record not found");
		}
		return null;
	}
}
