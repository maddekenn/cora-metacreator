package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

import java.util.List;

public class PGroupConstructor {

    private SpiderDataGroup pGroup;

    public SpiderDataGroup constructPGroupWithIdDataDividerPresentationOfAndChildren(String id, String dataDivider, String presentationOf, List<SpiderDataGroup> metadataChildReferences) {
//        pGroup = null;
        pGroup = SpiderDataGroup.withNameInData("presentation");
        createAndAddRecordInfoWithIdAndDataDivider(id, dataDivider);
        pGroup.addAttributeByIdWithValue("type", "pGroup");

        createAndAddPresentationOf(presentationOf);

        createAndAddChildren(metadataChildReferences);

        return pGroup;
    }

    private void createAndAddRecordInfoWithIdAndDataDivider(String id, String dataDivider) {
        SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
                dataDivider);
        pGroup.addChild(recordInfo);
    }

    private void createAndAddPresentationOf(String presentationOf) {
        SpiderDataGroup presentationOfGroup = SpiderDataGroup.withNameInData("presentationOf");
        presentationOfGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
        presentationOfGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", presentationOf));
        pGroup.addChild(presentationOfGroup);
    }

    private void createAndAddChildren(List<SpiderDataGroup> metadataChildReferences) {
        SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");

        for(SpiderDataGroup metadataChildReference : metadataChildReferences){
            SpiderDataGroup childReference = createChild(metadataChildReference);
            childReferences.addChild(childReference);
        }

        pGroup.addChild(childReferences);
    }

    private SpiderDataGroup createChild(SpiderDataGroup metadataChildReference) {
        SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
        childReference.setRepeatId(metadataChildReference.getRepeatId());

        SpiderDataGroup ref = createRef(metadataChildReference);

        childReference.addChild(ref);
        return childReference;
    }

    private SpiderDataGroup createRef(SpiderDataGroup metadataChildReference) {
        SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
        ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "presentation"));

        String presRefId = constructPresRefIdFromMetadataChild(metadataChildReference);
        ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", presRefId));
        return ref;
    }

    private String getMetadataRefId(SpiderDataGroup metadataChildReference) {
        SpiderDataGroup metadataRef = metadataChildReference.extractGroup("ref");
        return metadataRef.extractAtomicValue("linkedRecordId");
    }

    private String constructPresRefIdFromMetadataChild(SpiderDataGroup metadataChildReference) {
        String metadataRefId = getMetadataRefId(metadataChildReference);
        if(metadataChildIsCollectionVar(metadataRefId)){
           return constructPCollVarRefId(metadataRefId);
        }else if(metadataChildIsTextVariable(metadataRefId)){
            return constructPVarRefId(metadataRefId);
        }else if(metadataChildIsResourceLink(metadataRefId)){
            return constructPResLinkRefId(metadataRefId);
        }else if(metadataChildIsRecordLink(metadataRefId)){
            return constructPLinkRefId(metadataRefId);
        }else if(metadataChildIsGroup(metadataRefId)){
            return constructPGroupRefId(metadataRefId);
        }
        return "";
    }

    private boolean metadataChildIsCollectionVar(String metadataRefId) {
        return metadataRefId.endsWith("CollectionVar");
    }

    private String constructPCollVarRefId(String metadataRefId) {
        String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("CollectionVar"));
        return idPrefix.concat("PCollVar");
    }

    private boolean metadataChildIsTextVariable(String metadataRefId) {
        return metadataRefId.endsWith("TextVar");
    }

    private String constructPVarRefId(String metadataRefId) {
        String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("TextVar"));
        return idPrefix.concat("PVar");
    }

    private boolean metadataChildIsResourceLink(String metadataRefId) {
        return metadataRefId.endsWith("ResLink");
    }

    private String constructPResLinkRefId(String metadataRefId) {
        String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("ResLink"));
        return idPrefix.concat("PResLink");
    }

    private boolean metadataChildIsRecordLink(String metadataRefId) {
        return metadataRefId.endsWith("Link");
    }

    private String constructPLinkRefId(String metadataRefId) {
        String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("Link"));
        return idPrefix.concat("PLink");
    }


    private String constructPGroupRefId(String metadataRefId) {
        String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("Group"));
        return idPrefix.concat("PGroup");
    }

    private boolean metadataChildIsGroup(String metadataRefId) {
        return metadataRefId.endsWith("Group");
    }

}
