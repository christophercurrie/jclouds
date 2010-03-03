package org.jclouds.gogrid.services;

import com.google.common.util.concurrent.ListenableFuture;
import org.jclouds.gogrid.GoGrid;
import org.jclouds.gogrid.binders.BindIdsToQueryParams;
import org.jclouds.gogrid.binders.BindNamesToQueryParams;
import org.jclouds.gogrid.domain.ServerImage;
import org.jclouds.gogrid.filters.SharedKeyLiteAuthentication;
import org.jclouds.gogrid.functions.ParseImageFromJsonResponse;
import org.jclouds.gogrid.functions.ParseImageListFromJsonResponse;
import org.jclouds.gogrid.options.GetImageListOptions;
import org.jclouds.rest.annotations.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Set;

import static org.jclouds.gogrid.reference.GoGridHeaders.VERSION;
import static org.jclouds.gogrid.reference.GoGridQueryParams.IMAGE_KEY;
import static org.jclouds.gogrid.reference.GoGridQueryParams.IMAGE_DESCRIPTION_KEY;
import static org.jclouds.gogrid.reference.GoGridQueryParams.IMAGE_FRIENDLY_NAME_KEY;

/**
 * @author Oleksiy Yarmula
 */
@Endpoint(GoGrid.class)
@RequestFilters(SharedKeyLiteAuthentication.class)
@QueryParams(keys = VERSION, values = "1.4")
public interface GridImageAsyncClient {

    /**
     * @see GridImageClient#getImageList
     */
    @GET
    @ResponseParser(ParseImageListFromJsonResponse.class)
    @Path("/grid/image/list")
    ListenableFuture<Set<ServerImage>> getImageList(GetImageListOptions... options);

    /**
     * @see GridImageClient#getImagesById
     */
    @GET
    @ResponseParser(ParseImageListFromJsonResponse.class)
    @Path("/grid/image/get")
    ListenableFuture<Set<ServerImage>> getImagesById(@BinderParam(BindIdsToQueryParams.class) Long... ids);


    /**
     * @see GridImageClient#getImagesByName
     */
    @GET
    @ResponseParser(ParseImageListFromJsonResponse.class)
    @Path("/grid/image/get")
    ListenableFuture<Set<ServerImage>> getImagesByName(@BinderParam(BindNamesToQueryParams.class) String... names);

    /**
     * @see GridImageClient#editImageDescription
     */
    @GET
    @ResponseParser(ParseImageFromJsonResponse.class)
    @Path("/grid/image/edit")
    ListenableFuture<ServerImage> editImageDescription(@QueryParam(IMAGE_KEY) String idOrName,
                                                       @QueryParam(IMAGE_DESCRIPTION_KEY) String newDescription);

    /**
     * @see GridImageClient#editImageFriendlyName
     */
    @GET
    @ResponseParser(ParseImageFromJsonResponse.class)
    @Path("/grid/image/edit")
    ListenableFuture<ServerImage> editImageFriendlyName(@QueryParam(IMAGE_KEY) String idOrName,
                                                        @QueryParam(IMAGE_FRIENDLY_NAME_KEY) String newFriendlyName);

}
