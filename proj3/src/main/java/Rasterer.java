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
        double h = params.get("h");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double LonDPP = (lrlon - ullon) / w;

        if (! (ullon <= lrlon && ullat >= lrlat)) {
            results.put("query_success", false);
            return results;
        }

        int depth = depth(LonDPP);
        double lonGrid = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        double latGrid = (MapServer.ROOT_LRLAT - MapServer.ROOT_ULLAT) / Math.pow(2, depth);
//        System.out.println(lonGrid);
//        System.out.println(latGrid);
        double intersect_ullon = Math.max(ullon, MapServer.ROOT_ULLON);
        double intersect_ullat = Math.min(ullat, MapServer.ROOT_ULLAT);
        double intersect_lrlon = Math.min(lrlon, MapServer.ROOT_LRLON);
        double intersect_lrlat = Math.max(lrlat, MapServer.ROOT_LRLAT);

        if (! (intersect_ullon <= intersect_lrlon && intersect_ullat >= intersect_lrlat)) {
            results.put("query_success", false);
            return results;
        }

        int raster_ul_x_index = (int) ((intersect_ullon - MapServer.ROOT_ULLON) / lonGrid);
        int raster_ul_y_index = (int) ((intersect_ullat - MapServer.ROOT_ULLAT) / latGrid);
        int raster_lr_x_index = (int) Math.min((intersect_lrlon - MapServer.ROOT_ULLON) / lonGrid, Math.pow(2, depth) - 1);
        int raster_lr_y_index = (int) Math.min((intersect_lrlat - MapServer.ROOT_ULLAT) / latGrid, Math.pow(2, depth) - 1);
//        System.out.println(raster_ul_x_index);
//        System.out.println(raster_lr_x_index);
//        System.out.println(raster_ul_y_index);
//        System.out.println(raster_lr_y_index);

        String[][] render_grid = new String[raster_lr_y_index - raster_ul_y_index + 1][raster_lr_x_index - raster_ul_x_index + 1];
         for (int y = raster_ul_y_index; y <= raster_lr_y_index; y++) {
             for (int x = raster_ul_x_index; x <= raster_lr_x_index; x++) {
                render_grid[y - raster_ul_y_index][x - raster_ul_x_index] = "d"+ depth + "_x" + x +"_y" + y + ".png";
            }
        }
        double raster_ul_lon = MapServer.ROOT_ULLON + raster_ul_x_index * lonGrid;
        double raster_ul_lat = MapServer.ROOT_ULLAT + raster_ul_y_index * latGrid;
        double raster_lr_lon = MapServer.ROOT_ULLON + (raster_lr_x_index + 1) * lonGrid;
        double raster_lr_lat = MapServer.ROOT_ULLAT + (raster_lr_y_index + 1) * latGrid;

//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                           + "your browser.");
        results.put("depth", depth);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("render_grid", render_grid);
        results.put("query_success", true);
        //System.out.println(results);
        return results;
    }

    private int depth(double LonDPP) {
        double dpp = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        for (int i = 0; i < 8; i++) {
            if (dpp <= LonDPP) {
                return i;
            }
            dpp = dpp / 2;
        }
        return 7;
    }

}
