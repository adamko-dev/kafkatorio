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
  value class D1(val points: List<LatLng>) : PolylinePathPoints {
    constructor(points: Array<LatLng>)
        : this(points.toList())
  }

  value class D2(val points: List<List<LatLng>>) : PolylinePathPoints {
    constructor(points: Array<Array<LatLng>>)
        : this(points.map { x -> x.toList() }.toList())
  }

  value class D3(val points: List<List<List<LatLng>>>) : PolylinePathPoints {
    constructor(points: Array<Array<Array<LatLng>>>)
        : this(points.map { x -> x.map { y -> y.toList() } }.toList())
  }
}

inline fun Polyline<*>.getLatLngs2(): PolylinePathPoints? {
  return when (val points = this.getLatLngs()) {
    is Array<LatLng>               -> PolylinePathPoints.D1(points.unsafeCast<Array<LatLng>>())
    is Array<Array<LatLng>>        -> PolylinePathPoints.D2(points.unsafeCast<Array<Array<LatLng>>>())
    is Array<Array<Array<LatLng>>> -> PolylinePathPoints.D3(points.unsafeCast<Array<Array<Array<LatLng>>>>())
    else                           -> null
  }
}
