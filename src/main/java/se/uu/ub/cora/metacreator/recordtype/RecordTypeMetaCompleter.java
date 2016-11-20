package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class RecordTypeMetaCompleter implements ExtendedFunctionality {

    private String userId;
    private SpiderDataGroup spiderDataGroup;
    private String id;

    @Override
    public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
        this.userId = userId;
        this.spiderDataGroup = spiderDataGroup;
        
        addValuesToDataGroup();
	}
    
    private void addValuesToDataGroup(){
    	SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup("recordInfo");
    	id = recordInfoGroup.extractAtomicValue("id");
		addMetadataIds();
		addPresentationIds();
		addTexts();
    }

	private void addMetadataIds() {
		addAtomicValueWithNameInDataAndValueIfNotExisting("metadataId", id+"Group");
		addAtomicValueWithNameInDataAndValueIfNotExisting("newMetadataId", id+"NewGroup");
		addAtomicValueWithNameInDataAndValueIfNotExisting("searchMetadataId", id+"SearchGroup");
	}

	private void addAtomicValueWithNameInDataAndValueIfNotExisting(String nameInData, String value) {
		if(!spiderDataGroup.containsChildWithNameInData(nameInData)) {
			spiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue(nameInData, value));
		}
	}

	private void addPresentationIds() {
		addAtomicValueWithNameInDataAndValueIfNotExisting("presentationViewId", id+"ViewPGroup");

		addAtomicValueWithNameInDataAndValueIfNotExisting("presentationFormId", id+"FormPGroup");
		addAtomicValueWithNameInDataAndValueIfNotExisting("newPresentationFormId", id+"FormNewPGroup");
		addAtomicValueWithNameInDataAndValueIfNotExisting("menuPresentationViewId", id+"MenuPGroup");
		addAtomicValueWithNameInDataAndValueIfNotExisting("listPresentationViewId", id+"ListPGroup");
		addAtomicValueWithNameInDataAndValueIfNotExisting("selfPresentationViewId", id+"ViewSelfPGroup");
		addAtomicValueWithNameInDataAndValueIfNotExisting("searchPresentationFormId", id+"FormSearchPGroup");
	}

	private void addTexts() {
		addAtomicValueWithNameInDataAndValueIfNotExisting("textId", id+"Text");
		addAtomicValueWithNameInDataAndValueIfNotExisting("defTextId", id+"DefText");
	}


}
