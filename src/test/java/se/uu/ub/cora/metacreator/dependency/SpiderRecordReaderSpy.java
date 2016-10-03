/*
 * Copyright 2016 Olov McKie
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.metacreator.dependency;

import se.uu.ub.cora.spider.data.SpiderDataList;
import se.uu.ub.cora.spider.data.SpiderDataRecord;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class SpiderRecordReaderSpy implements SpiderRecordReader {
	public SpiderDataRecord record;

	@Override
	public SpiderDataRecord readRecord(String userId, String type, String id) {
		if ("text".equals(type) && "noTextsTextVarText".equals(id)) {
			throw new RecordNotFoundException("record not found in stub");
		}
		return null;
	}

	@Override
	public SpiderDataList readIncomingLinks(String userId, String type, String id) {
		if (null == record) {
			throw new RecordNotFoundException("Record not found");
		}
		return null;
	}

}
