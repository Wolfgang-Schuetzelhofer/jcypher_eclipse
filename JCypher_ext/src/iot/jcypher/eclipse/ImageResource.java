/************************************************************************
 * Copyright (c) 2014 IoT-Solutions e.U.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ************************************************************************/

package iot.jcypher.eclipse;

import java.io.InputStream;

import org.eclipse.core.internal.content.ContentType;
import org.eclipse.swt.graphics.Image;

public class ImageResource {

    /** The Constant ROOT_PATH_ICONS. */
    private static final String ROOT_PATH_ICONS = "/icons/";

    // START: Icons based on ContentType (vjdok.core.model.ContentType) of
    // Artifact.

    private static final String RN_JCYPHER = "jc_14_16.gif";

    public static final Image ICON_JCYPHER = ImageResource
            .createImage(ImageResource.RN_JCYPHER);

    /**
     * Constructor.
     */
    private ImageResource() {

    }

    /**
     * CreateImage.
     * 
     * @param resourceName
     *            the resourceName
     * @return the image
     */
    private static Image createImage(final String resourceName) {
        Image result = null;

        InputStream is = ImageResource.class
                .getResourceAsStream(ImageResource.ROOT_PATH_ICONS
                        + resourceName);
        if (is != null) {
            result = new Image(null, is);
        }

        return result;
    }
}
