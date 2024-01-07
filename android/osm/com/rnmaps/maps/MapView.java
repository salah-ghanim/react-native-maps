package com.rnmaps.maps;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;

import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

public class MapView extends org.osmdroid.views.MapView {

  private final MapManager manager;
  private final ThemedReactContext context;


  private static final String[] PERMISSIONS = new String[]{
      "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};


  public MapView(ThemedReactContext reactContext, ReactApplicationContext appContext,
                 MapManager manager) {
    super(appContext);
    this.addOnFirstLayoutListener(manager);
    this.manager = manager;
    this.context = reactContext;
    final ITileSource tileSource = TileSourceFactory.MAPNIK;
    setTileSource(tileSource);
  }

}
