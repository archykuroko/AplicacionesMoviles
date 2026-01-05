package ste.archykuroko.examenfinal.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun OsmMap(
    modifier: Modifier = Modifier,
    routePoints: List<GeoPoint>,
    currentPoint: GeoPoint?
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(17.0)
                controller.setCenter(currentPoint ?: GeoPoint(19.4326, -99.1332)) // CDMX default
            }
        },
        update = { map ->
            map.overlays.clear()

            // Ruta
            if (routePoints.size >= 2) {
                val line = Polyline().apply { setPoints(routePoints) }
                map.overlays.add(line)
            }

            // Marcador actual
            currentPoint?.let { gp ->
                val marker = Marker(map).apply {
                    position = gp
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Ubicación actual"
                }
                map.overlays.add(marker)
                map.controller.animateTo(gp)
            }

            map.invalidate()
        }
    )
}
