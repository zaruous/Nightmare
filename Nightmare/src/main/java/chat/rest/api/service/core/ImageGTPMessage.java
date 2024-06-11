/**
 * 
 */
package chat.rest.api.service.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.io.IOUtils;

/**
 * 
 */
public class ImageGTPMessage extends AbstractGTPMessage {
	private File image;

	public ImageGTPMessage() {
		super();
		this.setRole("user");
		this.setType("image_url");
	}

	public ImageGTPMessage(File image) {
		this();
		this.image = image;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	@Override
	public Map<String, Object> getRequestFormat() {
		byte[] b = null;
		try (FileInputStream fileInputStream = new FileInputStream(image)) {
			b = IOUtils.toByteArray(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (b != null) {
			String encodeToString = Base64.getEncoder().encodeToString(b);
			String content = String.format("data:image/png;base64,%s", encodeToString);
			return Map.of("type", "image_url", "image_url", Map.of("url", content));
		}
		return null;
	}
}
