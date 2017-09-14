package df.learn.MySpringFramework.modules.controllers;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import df.learn.MySpringFramework.commons.framework.AbstractController;
import df.learn.MySpringFramework.commons.utils.ContentTypeUtils;
import df.learn.MySpringFramework.commons.utils.FilenameUtils;
import df.learn.MySpringFramework.commons.utils.ImageThumbnails;
import df.learn.MySpringFramework.config.ApplicationConfiguration;
import df.learn.MySpringFramework.config.web.Response;
import df.learn.MySpringFramework.config.web.Response.ResponseBuilder;
import df.learn.MySpringFramework.config.web.Status;
import df.learn.MySpringFramework.modules.services.StorageService;
import df.learn.MySpringFramework.modules.storage.Store;

/**
 * @ClassName df.learn.MySpringFramework.modules.controllers.DemoFileController
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:34:04
 * @Author 854154025@qq.com
 * 
 * @Description 文件上传下载类。
 * 
 */
public class DemoFileController extends AbstractController {
	@Resource
	private StorageService service;

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, value = "upload/**")
	@ResponseBody
	public Response upload(MultipartHttpServletRequest request) {
		String location = request.getServletPath().substring("/file/upload/".length());
		Map<String, MultipartFile> map = request.getFileMap();
		if (map == null || map.isEmpty()) {
			return ResponseBuilder.create().status(Status.FAIL).build();
		}
		List<MultipartFile> files = new ArrayList<>(map.values());

		String md5 = null; // 文件MD5
		String ext = null; // 扩展名
		String uri = null;// 文件uri通常为文件MD5.后缀
		String contentType = null;// 文件类型
		// String contentDisposition = null;// 通知浏览器或下载器应向用户显示的名字，由于Store中的信息并未持久化，该属性暂时无用。
		String key = null;// 表现为存储根目录之内的相对文件路径，云存储中key即文件名（此文件名就是相对路径+filename）
		boolean isImage = false;// 检查文件是否是图像
		int side = 320;// 当文件是图像时， 限制其缩略图最大高宽为320，
		byte[] bytes = null; // 文件byte数据
		List<UploadResult> uploadResults = new ArrayList<>();
		UploadResult res = null;
		for (MultipartFile file : files) {
			try {
				bytes = file.getBytes();
				md5 = DigestUtils.md5Hex(bytes);// 获取文件md5值
			} catch (IOException e) {
				// 如果连MD5都获取失败了。。。 就不处理了。。
				continue;
			}

			ext = FilenameUtils.getExtention(file.getOriginalFilename());
			contentType = file.getContentType();
			if (StringUtils.isEmpty(contentType)) {
				contentType = ContentTypeUtils.getContentType(ext);
			}

			isImage = ContentTypeUtils.isImage(contentType);
			if (isImage) {
				ext = ImageThumbnails.FORMAT;// 图像格式统一使用jpg存档
			}
			//
			uri = md5.concat(".").concat(ext);
			// 下载时向用户显示的文件名字
			// contentDisposition = FileUtils.buildContentDisposition(uri);
			key = location.concat(File.separator).concat(uri);

			// 如果是图像，应先处理图像及修改其后缀名称为jpg。
			if (isImage) {
				// Java处理图像性能不高，当图像较大时，处理压缩耗时可能较久或占用内存较高。。
				// 所以当平台图像处理并发量较大时，应部署专用的图像处理程序，并放到线程中处理。
				// 扩展，添加Logo水印
				String thumbnailKeyUri = ApplicationConfiguration.THUMBNAIL_.concat(uri);
				String thumbnailKey = location.concat(File.separator).concat(thumbnailKeyUri);
				ByteArrayInputStream byteIn = null;
				ByteArrayOutputStream byteOut = null;
				Store thumbStore = null;
				try {
					if (!service.exists(thumbnailKey)) {
						byte[] thumbnail = ImageThumbnails.compressBySide(bytes, side);
						thumbStore = new Store();
						thumbStore.setObjectContent(new ByteArrayInputStream(thumbnail));
						thumbStore.setKey(thumbnailKey);
						thumbStore.setContentLength(thumbnail.length);
						thumbStore.setContentType(contentType);
						// thumbStore.setContentDisposition(FileUtils.buildContentDisposition(thumbnailKeyUri));
						service.putObject(thumbStore);// 若缩略图保存失败，不处理
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (byteIn != null) {
						try {
							byteIn.close();
						} catch (IOException e) {
						}
					}
					if (byteOut != null) {
						try {
							byteOut.close();
						} catch (IOException e) {
						}
					}
					if (thumbStore != null) {
						try {
							thumbStore.close();
						} catch (IOException e) {
						}
					}
				}
			}
			Store store = null;
			try {
				if (!service.exists(key)) {
					byte[] datas = null;
					if (isImage) {
						datas = ImageThumbnails.format(bytes);
					} else {
						datas = bytes;
					}
					store = new Store();
					store.setObjectContent(new ByteArrayInputStream(datas));
					store.setKey(key);
					store.setContentLength(datas.length);
					store.setContentType(contentType);
					// store.setContentDisposition(contentDisposition);
					if (!service.putObject(store)) {
						// 文件保存失败，返回失败信息。
						return ResponseBuilder.create().status(Status.FAIL).build();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (store != null) {
					try {
						store.close();
					} catch (Exception e2) {
					}
				}
			}
			res = new UploadResult();
			res.setExt(ext);
			res.setFileName(file.getName());
			res.setUri(uri);
			uploadResults.add(res);
		} // end
		return ResponseBuilder.create().data(res).build();

	}

	// @RequestMapping(method = RequestMethod.GET, value = "download/**")
	// public ModelAndView download(HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	// String key =
	// request.getServletPath().substring("/file/download/".length());
	// if (StringUtils.isEmpty(key)) {
	// return null;
	// }
	// return new ModelAndView("redirect:" + storage.getURL(key,
	// DateUtils.getInternalDateByHour(new Date(), 1)));
	// }

	@RequestMapping(method = RequestMethod.GET, value = "download/**")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = request.getServletPath().substring("/file/download/".length());
		if (key.indexOf("/private/") > -1) {
			Assert.notNull(null);
			return;
		}
		// if QClound
		// String url = service.getURL(key, null);
		// if (StringUtils.isNotEmpty(url)) {
		// response.sendRedirect(url);
		// return;
		// }

		Store store = service.getObject(key);
		// response.setContentLengthLong(store.getContentLength());//keke temp
		long fileLength = store.getContentLength();// 文件总长度
		if (fileLength < 1L) {
			return;
		}
		long passLength = 0L;// 记录已下载文件大小也就是要跳过的文件大小
		// int rangeSwitch = 0;//
		// 0：从头开始的全文下载；1：从指定字节开始下载余下全部；2：从某字节开始到某字节结束的下载（bytes=27000-39000）
		long toLength = 0L;// 记录客户端需要下载的字节段的最后一个字节偏移量（比如bytes=27000-39000，则这个值是为39000）
		long contentLength = 0L;// 客户端请求的字节总量
		String rangeBytes = "";// 记录客户端传来的形如“bytes=27000-”或者“bytes=27000-39000”的内容
		String range = request.getHeader("Range");
		String contentRange = "";
		int responseCode = HttpServletResponse.SC_OK;
		if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
			// 响应的格式是: Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
			rangeBytes = range.replaceAll("bytes=", "");
			// 不是从最开始下载，断点下载响应号为206
			responseCode = HttpServletResponse.SC_PARTIAL_CONTENT;
			if (rangeBytes.endsWith("-")) {// bytes=969998336-
				// rangeSwitch = 1;
				rangeBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
				passLength = Long.parseLong(rangeBytes.trim());
				// 客户端请求的是 969998336之后的字节（包括bytes下标索引为969998336的字节）
				contentLength = fileLength - passLength;
				contentRange = new StringBuilder("bytes ").append(passLength).append("-").append(fileLength - 1).append("/").append(fileLength).toString();
			} else {// bytes=1275856879-1275877358
				// rangeSwitch = 2;
				String temp0 = rangeBytes.substring(0, rangeBytes.indexOf('-'));
				String temp2 = rangeBytes.substring(rangeBytes.indexOf('-') + 1, rangeBytes.length());
				// bytes=1275856879-1275877358，从第1275856879个字节开始下载
				passLength = Long.parseLong(temp0.trim());
				toLength = Long.parseLong(temp2);// bytes=1275856879-1275877358，到第
													// 1275877358 个字节结束
				contentLength = toLength - passLength + 1;
				contentRange = range.replace("=", " ") + "/" + fileLength;
			}
		} else {// 从开始进行下载
			contentLength = fileLength;// 客户端要求全文下载
			contentRange = new StringBuilder("bytes ").append("0-").append(fileLength - 1).append("/").append(fileLength).toString();
		}

		// 设置response

		String contentType = store.getContentType();
		contentType = StringUtils.isEmpty(contentType) ? "application/octet-stream" : contentType;
		String contentDisposition = store.getContentDisposition();
		contentDisposition = StringUtils.isEmpty(contentDisposition) ? "attachment; filename=" + FilenameUtils.getOriginalFilename(store.getKey()) : contentDisposition;
		response.reset();//
		response.setContentType(contentType);
		response.setContentLengthLong(contentLength);// keke temp
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("Content-Disposition", contentDisposition);
		response.setHeader("Content-Range", contentRange);
		response.setStatus(responseCode);

		// 执行文件传输
		OutputStream out = null;
		BufferedInputStream in = null;
		try {
			if (store.getObjectContent() != null) {
				in = new BufferedInputStream(store.getObjectContent());
			} else if (store.getObject() != null) {
				in = new BufferedInputStream(new FileInputStream(store.getObject()));
			}
			if (in == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("NotMatchKey");
				response.getWriter().flush();
				return;
			}
			out = response.getOutputStream();
			in.skip(passLength);// 跳过已下载
			int bSize = 1024;
			long whileReadTotalLength = contentLength - bSize;
			byte[] b = new byte[bSize];
			int readLength = 0;
			int j = 0;
			while (readLength <= whileReadTotalLength) {
				j = in.read(b, 0, bSize);
				readLength += j;
				// 将b中的数据写到客户端的内存
				out.write(b, 0, j);
			}
			if (readLength <= contentLength) {// 余下的不足 1024 个字节在这里读取
				j = in.read(b, 0, (int) (contentLength - readLength));
				out.write(b, 0, j);
			}
			// 将写入到客户端的内存的数据,刷新到磁盘
			out.flush();
		} catch (Exception e) {

		} finally {
			if (store != null) {
				try {
					store.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
				}
			}
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	public class UploadResult {
		private String uri;
		private String ext;
		private String fileName;

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public String getExt() {
			return ext;
		}

		public void setExt(String ext) {
			this.ext = ext;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

	}
}
