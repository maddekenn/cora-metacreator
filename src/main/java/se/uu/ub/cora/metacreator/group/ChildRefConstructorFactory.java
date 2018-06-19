package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public interface ChildRefConstructorFactory {

	PChildRefConstructor factor(SpiderDataGroup metadataChildReference, String mode);

}
