/*******************************************************************************
 * Copyright (c) 2001, 2009 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Pavel Savara
 *     - Initial implementation
 *******************************************************************************/
package net.sf.robocode.repository2.items;


import net.sf.robocode.repository2.root.IRepositoryRoot;
import net.sf.robocode.repository.INamedFileSpecification;
import net.sf.robocode.io.Logger;
import net.sf.robocode.io.FileUtil;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;


/**
 * @author Pavel Savara (original)
 */
public class TeamItem extends NamedItem implements INamedFileSpecification {
	private final static String TEAM_DESCRIPTION = "team.description";
	private final static String TEAM_AUTHOR_NAME = "team.author.name";
	private final static String TEAM_AUTHOR_EMAIL = "team.author.email";
	private final static String TEAM_AUTHOR_WEBSITE = "team.author.website";
	private final static String TEAM_VERSION = "team.version";
	private final static String TEAM_WEBPAGE = "team.webpage";
	private final static String TEAM_MEMBERS = "team.members";
	private final static String TEAM_JAVA_SOURCE_INCLUDED = "team.java.source.included";
	private final static String ROBOCODE_VERSION = "robocode.version";

	private Properties properties = new Properties();
	private String teamFullName;

	public TeamItem(URL url, IRepositoryRoot root) {
		super(url, root);
		String tUrl = url.toString();

		tUrl = tUrl.substring(0, tUrl.lastIndexOf(".team"));
		final int versionSeparator = tUrl.lastIndexOf(" ");
		final int rootLen = root.getUrl().toString().length();

		if (versionSeparator != -1) {
			teamFullName = tUrl.substring(rootLen, versionSeparator).replace('/', '.').replace('\\', '.');
		} else {
			teamFullName = tUrl.substring(rootLen).replace('/', '.').replace('\\', '.');
		}
		isValid = true;
	}

	public List<String> getFriendlyUrls() {
		final ArrayList<String> urls = new ArrayList<String>();
		final String tUrl = url.toString();

		urls.add(tUrl.substring(0, tUrl.lastIndexOf('.')));
		urls.add(url.getFile());
		urls.add(getFullClassName());
		urls.add(getFullClassNameWithVersion());
		urls.add(getUniqueFullClassNameWithVersion());
		return urls;
	}

	public void update(long lastModified, boolean force) {
		if (lastModified > this.lastModified || force) {
			this.lastModified = lastModified;
			loadProperties();
		}
	}

	private void loadProperties() {
		if (url != null) {
			InputStream ios = null;

			try {
				ios = url.openStream();
				properties.load(ios);
			} catch (IOException e) {
				Logger.logError(e);
			} finally {
				FileUtil.cleanupStream(ios);
			}
		}
	}

	public String getFullClassName() {
		return teamFullName;
	}

	public String getMembers() {
		return properties.getProperty(TEAM_MEMBERS, null);
	}

	public String getVersion() {
		return properties.getProperty(TEAM_VERSION, null);
	}

	public String getDescription() {
		return properties.getProperty(TEAM_DESCRIPTION, null);
	}

	public String getAuthorName() {
		return properties.getProperty(TEAM_AUTHOR_NAME, null);
	}

	public URL getWebpage() {
		try {
			return new URL(properties.getProperty(TEAM_AUTHOR_WEBSITE, null));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public String getRobocodeVersion() {
		return properties.getProperty(ROBOCODE_VERSION, null);
	}

	public String toString() {
		return url.toString();
	}

}