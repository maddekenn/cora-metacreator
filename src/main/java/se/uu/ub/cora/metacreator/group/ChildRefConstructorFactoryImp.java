package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.record.DataException;

public class ChildRefConstructorFactoryImp implements ChildRefConstructorFactory {

	@Override
	public ChildRefConstructor factor(SpiderDataGroup metadataChildReference, String mode) {
		String metadataRefId = getMetadataRefId(metadataChildReference);
		if (metadataChildIsTextVariable(metadataRefId)) {
			return PVarChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		} else if (metadataChildIsCollectionVar(metadataRefId)) {
			return PCollVarChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		} else if (metadataChildIsResourceLink(metadataRefId)) {
			return PResLinkChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		} else if (metadataChildIsRecordLink(metadataRefId)) {
			return PLinkChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		} else if (metadataChildIsGroup(metadataRefId)) {
			return PGroupChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		}
		throw new DataException("Not possible to construct childReferenceId from metadataId");
	}

	private String getMetadataRefId(SpiderDataGroup metadataChildReference) {
		SpiderDataGroup metadataRef = metadataChildReference.extractGroup("ref");
		return metadataRef.extractAtomicValue("linkedRecordId");
	}

	private boolean metadataChildIsTextVariable(String metadataRefId) {
		return metadataRefId.endsWith("TextVar");
	}

	private boolean metadataChildIsCollectionVar(String metadataRefId) {
		return metadataRefId.endsWith("CollectionVar");
	}

	private boolean metadataChildIsResourceLink(String metadataRefId) {
		return metadataRefId.endsWith("ResLink");
	}

	private boolean metadataChildIsRecordLink(String metadataRefId) {
		return metadataRefId.endsWith("Link");
	}

	private boolean metadataChildIsGroup(String metadataRefId) {
		return metadataRefId.endsWith("Group");
	}

}
