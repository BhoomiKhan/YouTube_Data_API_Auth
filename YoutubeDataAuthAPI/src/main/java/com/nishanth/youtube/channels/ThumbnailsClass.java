package com.nishanth.youtube.channels;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ThumbnailsClass {
	
	@JsonProperty(value = "default")
	private DefaultClass defaults;

	public DefaultClass getDefaults() {
		return defaults;
	}
	public void setDefaults(DefaultClass defaults) {
		this.defaults = defaults;
	}
}
