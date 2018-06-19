package se.uu.ub.cora.metacreator.group;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class ChildRefConstructorFactorySpy implements ChildRefConstructorFactory {

	public List<PChildRefConstructor> factored = new ArrayList<>();

	@Override
	public PChildRefConstructor factor(SpiderDataGroup metadataChildReference, String mode) {
		PChildRefConstructor pChildRefConstructor = new PChildRefConstructorSpy(
				metadataChildReference, mode);
		factored.add(pChildRefConstructor);
		return pChildRefConstructor;
	}

}
