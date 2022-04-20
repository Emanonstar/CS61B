import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double w = params.get("w");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double lonDPP = (lrlon - ullon) / w;

        // Nothing to raster.
        if (!(ullon <= lrlon && ullat >= lrlat)) {
            results.put("query_success", false);
            return results;
        }

        int depth = depth(lonDPP);
        double lonGrid = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        double latGrid = (MapServer.ROOT_LRLAT - MapServer.ROOT_ULLAT) / Math.pow(2, depth);

        // Calculate the intersect area.
        double intersectUllon = Math.max(ullon, MapServer.ROOT_ULLON);
        double intersectUllat = Math.min(ullat, MapServer.ROOT_ULLAT);
        double intersectLrlon = Math.min(lrlon, MapServer.ROOT_LRLON);
        double intersectLrlat = Math.max(lrlat, MapServer.ROOT_LRLAT);

        // Nothing to raster.
        if (!(intersectUllon <= intersectLrlon && intersectUllat >= intersectLrlat)) {
            results.put("query_success", false);
            return results;
        }

        int rasterUlXIndex = (int) ((intersectUllon - MapServer.ROOT_ULLON) / lonGrid);
        int rasterUlYIndex = (int) ((intersectUllat - MapServer.ROOT_ULLAT) / latGrid);
        int rasterLrXIndex = (int) Math.min((intersectLrlon - MapServer.ROOT_ULLON)
                / lonGrid, Math.pow(2, depth) - 1);
        int rasterLrYIndex = (int) Math.min((intersectLrlat - MapServer.ROOT_ULLAT)
                / latGrid, Math.pow(2, depth) - 1);

        String[][] renderGrid = new String[rasterLrYIndex - rasterUlYIndex + 1]
                [rasterLrXIndex - rasterUlXIndex + 1];
        for (int y = rasterUlYIndex; y <= rasterLrYIndex; y++) {
            for (int x = rasterUlXIndex; x <= rasterLrXIndex; x++) {
                renderGrid[y - rasterUlYIndex][x - rasterUlXIndex] =
                        "d" + depth + "_x" + x + "_y" + y + ".png";
            }
        }
        double rasterUlLon = MapServer.ROOT_ULLON + rasterUlXIndex * lonGrid;
        double rasterUlLat = MapServer.ROOT_ULLAT + rasterUlYIndex * latGrid;
        double rasterLrLon = MapServer.ROOT_ULLON + (rasterLrXIndex + 1) * lonGrid;
        double rasterLrLat = MapServer.ROOT_ULLAT + (rasterLrYIndex + 1) * latGrid;

        results.put("depth", depth);
        results.put("raster_ul_lon", rasterUlLon);
        results.put("raster_ul_lat", rasterUlLat);
        results.put("raster_lr_lon", rasterLrLon);
        results.put("raster_lr_lat", rasterLrLat);
        results.put("render_grid", renderGrid);
        results.put("query_success", true);
        return results;
    }

    /** Return the best depth of the nodes of the rastered image with required LONDPP. */
    private int depth(double lonDPP) {
        double dpp = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        for (int i = 0; i < 8; i++) {
            if (dpp <= lonDPP) {
                return i;
            }
            dpp = dpp / 2;
        }
        return 7;
    }

}
