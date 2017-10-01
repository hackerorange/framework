package com.ab.us.framework.core.utils.support;

public interface ObjectResolvable {

	public static final ObjectResolvable Nothing_Resolver = new ObjectResolvable() {

		public Object resolve(Object item) {

			return item;
		}

	};

	public Object resolve(Object item);
}
