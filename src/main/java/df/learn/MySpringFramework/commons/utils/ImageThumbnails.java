package df.learn.MySpringFramework.commons.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.filters.ImageFilter;

/**
 * @ClassName df.learn.MySpringFramework.commons.utils.ImageThumbnails
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午4:28:25
 * @Author 854154025@qq.com
 * 
 * @Description 图像压缩类，该工具类可添加水印。。。。{@link ThumbnailParameter} <br>
 * 
 */
public class ImageThumbnails {

	/**
	 * @Fields filter : 图像过滤器，主要用于将PNG图像中透明背景设置为白色
	 */
	private static final ImageFilter filter = new PNGImageFilter();
	/**
	 * @Fields FORMAT : 指定的图像格式
	 */
	public static final String FORMAT = "jpg";

	/**
	 * @Methods read
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 * 
	 * @Description 从流中读取图像对象{@link BufferedImage}
	 */
	public static BufferedImage read(InputStream in) throws IOException {
		return ImageIO.read(in);
	}

	/**
	 * @Methods format
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 * 
	 * @Description 将图片转换成jpg格式
	 */
	public static byte[] format(byte[] data) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
		BufferedImage image = read(byteIn);
		if (image == null) {
			return new byte[0];
		}
		return compress(data, image.getWidth(), image.getHeight());
	}

	public static BufferedImage compressBySide(BufferedImage image, int side) throws IOException {
		if (image == null) {
			return null;
		}
		int width = image.getWidth();
		int height = image.getHeight();
		Builder<BufferedImage> builder = Thumbnails.of(image);
		// 限制最大长度，如果高宽小于side 则不要放大图像
		if (width > height) {
			builder.width(width < side ? width : side);
		} else if (width == height) {
			side = width < side ? width : side;
			builder.size(side, side);
		} else {
			builder.height(height < side ? height : side);
		}
		return builder.outputFormat(FORMAT).addFilter(filter).asBufferedImage();
	}

	public static byte[] compressBySide(byte[] data, int side) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
		BufferedImage image = read(byteIn);
		if (image == null) {
			return new byte[0];
		}
		int width = image.getWidth();
		int height = image.getHeight();
		Builder<BufferedImage> builder = Thumbnails.of(image);
		// 限制最大长度，如果高宽小于side 则不要放大图像
		if (width > height) {
			builder.width(width < side ? width : side);
		} else if (width == height) {
			side = width < side ? width : side;
			builder.size(side, side);
		} else {
			builder.height(height < side ? height : side);
		}
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		builder.outputFormat(FORMAT).addFilter(filter).toOutputStream(byteOut);
		byte[] result = byteOut.toByteArray();
		byteIn.close();
		byteOut.close();
		return result;
	}

	/**
	 * @Methods compressByWidth
	 * 
	 * @param in
	 * @param width
	 * @return
	 * @throws IOException
	 * 
	 * @Description 指定宽度压缩
	 */
	public static ByteArrayOutputStream compressByWidth(InputStream in, int width) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		Thumbnails.of(in).width(width).outputFormat(FORMAT).addFilter(filter).toOutputStream(byteOut);
		return byteOut;
	}

	/**
	 * @Methods compressByWidth
	 * 
	 * @param image
	 * @param width
	 * @return
	 * @throws IOException
	 * 
	 * @Description 指定宽度压缩
	 */
	public static BufferedImage compressByWidth(BufferedImage image, int width) throws IOException {
		return Thumbnails.of(image).width(width).outputFormat(FORMAT).addFilter(filter).asBufferedImage();
	}

	/**
	 * @Methods compressByWidth
	 * 
	 * @param data
	 * @param width
	 * @return
	 * @throws IOException
	 * 
	 * @Description 指定宽度压缩
	 */
	public static byte[] compressByWidth(byte[] data, int width) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		Thumbnails.of(byteIn).width(width).outputFormat(FORMAT).addFilter(filter).toOutputStream(byteOut);
		byte[] result = byteOut.toByteArray();
		byteIn.close();
		byteOut.close();
		return result;
	}

	/**
	 * @Methods compressByHeight
	 * 
	 * @param image
	 * @param height
	 * @return
	 * @throws IOException
	 * 
	 * @Description 指定高度压缩
	 */
	public static BufferedImage compressByHeight(BufferedImage image, int height) throws IOException {
		return Thumbnails.of(image).width(height).outputFormat(FORMAT).addFilter(filter).asBufferedImage();
	}

	/**
	 * @Methods compressByWidth
	 * 
	 * @param data
	 * @param width
	 * @return
	 * @throws IOException
	 * 
	 * @Description 指定高度压缩
	 */
	public static byte[] compressByHeight(byte[] data, int height) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		Thumbnails.of(byteIn).height(height).outputFormat(FORMAT).addFilter(filter).toOutputStream(byteOut);
		byte[] result = byteOut.toByteArray();
		byteIn.close();
		byteOut.close();
		return result;
	}

	/**
	 * @Methods compress
	 * 
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 * 
	 * @Description 指定高宽进行压缩
	 */
	public static byte[] compress(byte[] data, int width, int height) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		Thumbnails.of(byteIn).size(width, height).outputFormat(FORMAT).addFilter(filter).toOutputStream(byteOut);
		byte[] result = byteOut.toByteArray();
		byteIn.close();
		byteOut.close();
		return result;
	}

	/**
	 * @Methods compress
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 * 
	 * @Description 指定高宽进行压缩
	 */
	public static BufferedImage compress(BufferedImage image, int width, int height) throws IOException {
		return Thumbnails.of(image).size(width, height).outputFormat(FORMAT).addFilter(filter).asBufferedImage();
	}

	/**
	 * @Methods compress
	 * 
	 * @param data
	 * @param scale
	 * @return
	 * @throws IOException
	 * 
	 * @Description 指定压缩比例进行压缩，如1000*1000的图像，scale为0.75时，压缩的图像为750*750
	 */
	public static byte[] compress(byte[] data, float scale) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		Thumbnails.of(byteIn).scale(scale).outputFormat(FORMAT).addFilter(filter).toOutputStream(byteOut);
		byte[] result = byteOut.toByteArray();
		byteIn.close();
		byteOut.close();
		return result;
	}

	/**
	 * @Methods compress
	 * 
	 * @param data
	 * @param scale
	 * @param quality
	 * @return
	 * @throws IOException
	 * 
	 * @Description 根据质量进行压缩，同时应指定压缩比例
	 */
	public static byte[] compress(byte[] data, float scale, double quality) throws IOException {
		if (quality < 0.0D || quality > 1.0D) {
			quality = ThumbnailParameter.DEFAULT_QUALITY;
		}
		ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		Thumbnails.of(byteIn).scale(scale).outputQuality(quality).outputFormat(FORMAT).addFilter(filter).toOutputStream(byteOut);
		byte[] result = byteOut.toByteArray();
		byteIn.close();
		byteOut.close();
		return result;
	}

	public static class PNGImageFilter implements ImageFilter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see net.coobird.thumbnailator.filters.ImageFilter#apply(java.awt.image.BufferedImage)
		 * 
		 * @Methods apply
		 * 
		 * @param img
		 * 
		 * @return
		 * 
		 * @Description TODO
		 */
		@Override
		public BufferedImage apply(BufferedImage img) {
			BufferedImage image2 = null;
			if (img.getColorModel().getTransparency() != Transparency.OPAQUE) {
				int w = img.getWidth();
				int h = img.getHeight();
				image2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image2.createGraphics();
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, w, h);
				g.drawRenderedImage(img, null);
				g.dispose();
			}
			return image2 == null ? img : image2;
		}

	}
}
