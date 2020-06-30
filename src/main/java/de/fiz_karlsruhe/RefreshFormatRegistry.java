package de.fiz_karlsruhe;

import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Transformation;

public class RefreshFormatRegistry implements Runnable {

	final static Logger logger = LogManager.getLogger(RefreshFormatRegistry.class);

	FormatRegistry formatRegistry;

	Properties properties;

	RefreshFormatRegistry(FormatRegistry formatRegistry, Properties properties) {
		logger.info("Create new RefreshFormatRegistry object");
		this.formatRegistry = formatRegistry;
		this.properties = properties;
	}

	public void run() {
		logger.info("RefreshFormatRegistry.run() Refresh formats and transformations");
		List<Format> formats = FizRecordFactory.initFormats(properties);
		List<Transformation> transformations = FizRecordFactory.initTransformations(properties);

		if (formats != null && formats.size() > 0) {
			this.formatRegistry.setFormats(formats);
		}

		if (transformations != null && transformations.size() > 0) {
			this.formatRegistry.setTransformations(transformations);
		}
	}
}