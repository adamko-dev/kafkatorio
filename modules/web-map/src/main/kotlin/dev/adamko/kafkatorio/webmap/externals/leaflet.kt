package dev.adamko.kafkatorio.webmap.externals

import io.kvision.maps.externals.leaflet.DoneCallback
import io.kvision.maps.externals.leaflet.geo.LatLng
import io.kvision.maps.externals.leaflet.layer.tile.TileLayer
import io.kvision.maps.externals.leaflet.layer.vector.Polyline
import org.w3c.dom.HTMLElement


inline var TileLayer<TileLayer.TileLayerOptions>.tileOnLoad: TileOnLoadFn?
  get() = asDynamic()._tileOnLoad as TileOnLoadFn
  set(value) {
    asDynamic()._tileOnLoad = value
  }


typealias TileOnLoadFn = (done: DoneCallback, tile: HTMLElement) -> Unit


sealed interface PolylinePathPoints {
  value class D1(val points: Array<LatLng>) : PolylinePathPoints
  value class D2(val points: Array<Array<LatLng>>) : PolylinePathPoints
  value class D3(val points: Array<Array<Array<LatLng>>>) : PolylinePathPoints
}

inline fun Polyline<*>.getLatLngs2(): PolylinePathPoints? {
  return when (val points = this.getLatLngs()) {
    is Array<LatLng>               -> PolylinePathPoints.D1(points as Array<LatLng>)
    is Array<Array<LatLng>>        -> PolylinePathPoints.D2(points as Array<Array<LatLng>>)
    is Array<Array<Array<LatLng>>> -> PolylinePathPoints.D3(points as Array<Array<Array<LatLng>>>)
    else                           -> null
  }
}
