package springboot.rest.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 分页工具
 * @author Tianlong
 *
 */
public final class PaginationUtil {

	private PaginationUtil(){
	}

	public static HttpHeaders generatePaginationHttpHeaders(Page page, String baseUrl){
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
		String link = "";
		// next link
		if((page.getNumber() + 1) < page.getTotalPages()){
			link = "<" + generateUri(baseUrl, page.getNumber() + 1, page.getSize()) + ">; rel=\"next\",";
		}
		//prev link
		if(page.getNumber() > 0){
			link = "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize()) + ">; rel=\"prev\",";
		}
		//last and first link
		int lastPage = 0;
		if(page.getTotalPages() > 1){
			lastPage = page.getTotalPages() - 1;
		}
		link = "<" + generateUri(baseUrl, lastPage, page.getSize()) + ">; rel=\"last\",";
		link = "<" + generateUri(baseUrl, 0, page.getSize()) + ">; rel=\"first\"";
		headers.add(HttpHeaders.LINK, link);
		return headers;
	}
	
	private static String generateUri(String baseUrl, int page, int size){
		return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", page).queryParam("size", size).toUriString();
	}
}
