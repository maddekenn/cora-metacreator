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
		if ("textSystemOne".equals(type)) {
			switch (id) {
			case "textIdTextsInStorageTextVarText":
			case "textIdTextsInStorageTextVarDefText":
			case "textIdOnlyTextInStorageTextVarText":
			case "textIdNoPVarsInStorageTextVarPVar":
			case "textIdNoPVarsInStorageTextVarOutputPVar":
			case "myRecordType2Text":
			case "myRecordType2DefText":
			case "someExistingTextId":
			case "someExistingDefTextId":
			case "someExistingText":
			case "someExistingDefText":
				return null;
			default:
				throw new RecordNotFoundException("record not found in stub");
			}
		}
		if ("presentationVar".equals(type)) {
			switch (id) {
			case "textIdInputPVarInStorageTextVarPVar":
			case "textIdOutputPVarInStorageTextVarOutputPVar":
				return null;
			default:
				throw new RecordNotFoundException("record not found in stub");
			}
		}
		if ("presentationGroup".equals(type)) {
			switch (id) {
			case "myRecordType2ViewPGroup":
			case "myRecordType2FormPGroup":
			case "myRecordType2FormNewPGroup":
			case "myRecordType2MenuPGroup":
			case "myRecordType2ListPGroup":
				return null;
			default:
				throw new RecordNotFoundException("record not found in stub");
			}
		}
		if ("metadataGroup".equals(type)) {
			switch (id) {
			case "myRecordType2Group":
			case "myRecordType2NewGroup":
				return null;
			default:
				throw new RecordNotFoundException("record not found in stub");
			}
		}
		if ("search".equals(type)) {
			switch (id) {
			case "myRecordTypeSearch":
				throw new RecordNotFoundException("record not found in stub");
			default:
				return null;
			}
		}
		if ("metadataCollectionItem".equals(type)) {

			switch (id) {
			case "alreadyExistItem":
				return null;
			default:
				throw new RecordNotFoundException("record not found in stub");
			}
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
