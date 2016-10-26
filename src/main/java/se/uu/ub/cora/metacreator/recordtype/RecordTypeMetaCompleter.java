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
		addAtomicValueWithNameInDataAndValueEnding("metadataId", "Group");
		addAtomicValueWithNameInDataAndValueEnding("newMetadataId", "NewGroup");
		addAtomicValueWithNameInDataAndValueEnding("presentationViewId", "ViewPGroup");
		addAtomicValueWithNameInDataAndValueEnding("presentationFormId", "FormPGroup");
		addAtomicValueWithNameInDataAndValueEnding("newPresentationFormId", "FormNewPGroup");
		addAtomicValueWithNameInDataAndValueEnding("menuPresentationViewId", "MenuPGroup");
		addAtomicValueWithNameInDataAndValueEnding("listPresentationViewId", "ListPGroup");
		addAtomicValueWithNameInDataAndValueEnding("searchMetadataId", "SearchGroup");
		addAtomicValueWithNameInDataAndValueEnding("searchPresentationFormId", "FormSearchPGroup");
		addAtomicValueWithNameInDataAndValueEnding("selfPresentationViewId", "ViewSelfPGroup");
		addAtomicValueWithNameInDataAndValueEnding("textId", "Text");
		addAtomicValueWithNameInDataAndValueEnding("defTextId", "DefText");
		spiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("permissionKey", "RECORDTYPE_"+id.toUpperCase()));
    }

	private void addAtomicValueWithNameInDataAndValueEnding(String nameInData, String valueEnding) {
		spiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue(nameInData, id+valueEnding));
	}

}
