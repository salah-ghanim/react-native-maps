package com.rnmaps.maps;

import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.CustomZoomButtonsController;

public class MapManager extends ViewGroupManager<MapView>  implements org.osmdroid.views.MapView.OnFirstLayoutListener {

  private static final String REACT_CLASS = "AIROSMMap";



  private final ReactApplicationContext appContext;

  private BoundingBox initialBoundingBox;


  public MapManager(ReactApplicationContext context) {
    this.appContext = context;
    Configuration.getInstance().setUserAgentValue("com.react-native-maps.testapp");
  }


  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected MapView createViewInstance(@NonNull ThemedReactContext context) {
    return new MapView(context, this.appContext, this);
  }


  private void emitMapError(ThemedReactContext context, String message, String type) {
    WritableMap error = Arguments.createMap();
    error.putString("message", message);
    error.putString("type", type);

    context
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit("onError", error);
  }

  @ReactProp(name = "zoomEnabled", defaultBoolean = true)
  public void setZoomEnabled(MapView view, boolean zoomEnabled) {
    view.getZoomController().setZoomInEnabled(zoomEnabled);

  }

  @ReactProp(name = "zoomControlEnabled", defaultBoolean = true)
  public void setZoomControlEnabled(MapView view, boolean zoomControlEnabled) {
    view.getZoomController().setVisibility(zoomControlEnabled ? CustomZoomButtonsController.Visibility.ALWAYS : CustomZoomButtonsController.Visibility.NEVER);
  }

  @ReactProp(name = "minZoomLevel")
  public void setMinZoomLevel(MapView view, float minZoomLevel) {
    view.setMinZoomLevel((double) minZoomLevel);
  }

  @ReactProp(name = "maxZoomLevel")
  public void setMaxZoomLevel(MapView view, float maxZoomLevel) {
    view.setMaxZoomLevel((double) maxZoomLevel);
  }
  private BoundingBox boundingBoxFromRegionMap(ReadableMap region){
    double lng = region.getDouble("longitude");
    double lat = region.getDouble("latitude");
    double lngDelta = region.getDouble("longitudeDelta");
    double latDelta = region.getDouble("latitudeDelta");

    return new BoundingBox(lat + (latDelta / 2), lng + (lngDelta / 2) , lat - (latDelta / 2), lng - (lngDelta / 2));
  }
  @ReactProp(name = "initialRegion")
  public void setInitialRegion(MapView view, ReadableMap region) {
      safelySetRegion(view, region);
  }

  private void safelySetRegion(MapView view, ReadableMap region){
    if (region != null) {
      if (view.isLayoutOccurred()) {
        view.zoomToBoundingBox(boundingBoxFromRegionMap(region), true);
      } else {
        initialBoundingBox = boundingBoxFromRegionMap(region);
      }
    }
  }

  @ReactProp(name = "region")
  public void setRegion(MapView view, ReadableMap region) {
    safelySetRegion(view, region);
  }

  /*


    @ReactProp(name = "zoomTapEnabled", defaultBoolean = true)
  public void setZoomTapEnabled(MapView view, boolean zoomEnabled) {
    view.getZoomController().setZoomInEnabled(zoomEnabled);

  }



  @ReactProp(name = "camera")
  public void setCamera(MapView view, ReadableMap camera) {
    view.setCamera(camera);
  }

  @ReactProp(name = "initialCamera")
  public void setInitialCamera(MapView view, ReadableMap initialCamera) {
    view.setInitialCamera(initialCamera);
  }

  @ReactProp(name = "mapPadding")
  public void setMapPadding(MapView view, @Nullable ReadableMap padding) {
    int left = 0;
    int top = 0;
    int right = 0;
    int bottom = 0;
    double density = (double) view.getResources().getDisplayMetrics().density;

    if (padding != null) {
      if (padding.hasKey("left")) {
        left = (int) (padding.getDouble("left") * density);
      }

      if (padding.hasKey("top")) {
        top = (int) (padding.getDouble("top") * density);
      }

      if (padding.hasKey("right")) {
        right = (int) (padding.getDouble("right") * density);
      }

      if (padding.hasKey("bottom")) {
        bottom = (int) (padding.getDouble("bottom") * density);
      }
    }

    view.applyBaseMapPadding(left, top, right, bottom);
    view.map.setPadding(left, top, right, bottom);
  }

  @ReactProp(name = "showsUserLocation", defaultBoolean = false)
  public void setShowsUserLocation(MapView view, boolean showUserLocation) {
    view.setShowsUserLocation(showUserLocation);
  }

  @ReactProp(name = "userLocationPriority")
  public void setUserLocationPriority(MapView view, @Nullable String accuracy) {
    view.setUserLocationPriority(MY_LOCATION_PRIORITY.get(accuracy));
  }

  @ReactProp(name = "userLocationUpdateInterval", defaultInt = 5000)
  public void setUserLocationUpdateInterval(MapView view, int updateInterval) {
    view.setUserLocationUpdateInterval(updateInterval);
  }

  @ReactProp(name = "userLocationFastestInterval", defaultInt = 5000)
  public void setUserLocationFastestInterval(MapView view, int fastestInterval) {
    view.setUserLocationFastestInterval(fastestInterval);
  }

  @ReactProp(name = "showsMyLocationButton", defaultBoolean = true)
  public void setShowsMyLocationButton(MapView view, boolean showMyLocationButton) {
    view.setShowsMyLocationButton(showMyLocationButton);
  }

  @ReactProp(name = "toolbarEnabled", defaultBoolean = true)
  public void setToolbarEnabled(MapView view, boolean toolbarEnabled) {
    view.setToolbarEnabled(toolbarEnabled);
  }

  // This is a private prop to improve performance of panDrag by disabling it when the callback
  // is not set
  @ReactProp(name = "handlePanDrag", defaultBoolean = false)
  public void setHandlePanDrag(MapView view, boolean handlePanDrag) {
    view.setHandlePanDrag(handlePanDrag);
  }

  @ReactProp(name = "showsTraffic", defaultBoolean = false)
  public void setShowTraffic(MapView view, boolean showTraffic) {
    view.map.setTrafficEnabled(showTraffic);
  }

  @ReactProp(name = "showsBuildings", defaultBoolean = false)
  public void setShowBuildings(MapView view, boolean showBuildings) {
    view.map.setBuildingsEnabled(showBuildings);
  }

  @ReactProp(name = "showsIndoors", defaultBoolean = false)
  public void setShowIndoors(MapView view, boolean showIndoors) {
    view.map.setIndoorEnabled(showIndoors);
  }

  @ReactProp(name = "showsIndoorLevelPicker", defaultBoolean = false)
  public void setShowsIndoorLevelPicker(MapView view, boolean showsIndoorLevelPicker) {
    view.map.getUiSettings().setIndoorLevelPickerEnabled(showsIndoorLevelPicker);
  }

  @ReactProp(name = "showsCompass", defaultBoolean = false)
  public void setShowsCompass(MapView view, boolean showsCompass) {
    view.map.getUiSettings().setCompassEnabled(showsCompass);
  }

  @ReactProp(name = "scrollEnabled", defaultBoolean = false)
  public void setScrollEnabled(MapView view, boolean scrollEnabled) {
    view.map.getUiSettings().setScrollGesturesEnabled(scrollEnabled);
  }


  @ReactProp(name = "rotateEnabled", defaultBoolean = false)
  public void setRotateEnabled(MapView view, boolean rotateEnabled) {
    view.map.getUiSettings().setRotateGesturesEnabled(rotateEnabled);
  }

  @ReactProp(name = "scrollDuringRotateOrZoomEnabled", defaultBoolean = true)
  public void setScrollDuringRotateOrZoomEnabled(MapView view, boolean scrollDuringRotateOrZoomEnabled) {
    view.map.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(scrollDuringRotateOrZoomEnabled);
  }

  @ReactProp(name = "cacheEnabled", defaultBoolean = false)
  public void setCacheEnabled(MapView view, boolean cacheEnabled) {
    view.setCacheEnabled(cacheEnabled);
  }

  @ReactProp(name = "loadingEnabled", defaultBoolean = false)
  public void setLoadingEnabled(MapView view, boolean loadingEnabled) {
    view.enableMapLoading(loadingEnabled);
  }

  @ReactProp(name = "moveOnMarkerPress", defaultBoolean = true)
  public void setMoveOnMarkerPress(MapView view, boolean moveOnPress) {
    view.setMoveOnMarkerPress(moveOnPress);
  }

  @ReactProp(name = "loadingBackgroundColor", customType = "Color")
  public void setLoadingBackgroundColor(MapView view, @Nullable Integer loadingBackgroundColor) {
    view.setLoadingBackgroundColor(loadingBackgroundColor);
  }

  @ReactProp(name = "loadingIndicatorColor", customType = "Color")
  public void setLoadingIndicatorColor(MapView view, @Nullable Integer loadingIndicatorColor) {
    view.setLoadingIndicatorColor(loadingIndicatorColor);
  }

  @ReactProp(name = "pitchEnabled", defaultBoolean = false)
  public void setPitchEnabled(MapView view, boolean pitchEnabled) {
    view.map.getUiSettings().setTiltGesturesEnabled(pitchEnabled);
  }



  @ReactProp(name = "kmlSrc")
  public void setKmlSrc(MapView view, String kmlUrl) {
    if (kmlUrl != null) {
      view.setKmlSrc(kmlUrl);
    }
  }

  @Override
  public void receiveCommand(@NonNull MapView view, String commandId, @Nullable ReadableArray args) {
    int duration;
    double lat;
    double lng;
    double lngDelta;
    double latDelta;
    ReadableMap region;
    ReadableMap camera;

    switch (commandId) {
      case "setCamera":
        if(args == null) {
          break;
        }
        camera = args.getMap(0);
        view.animateToCamera(camera, 0);
        break;

      case "animateCamera":
        if(args == null) {
          break;
        }
        camera = args.getMap(0);
        duration = args.getInt(1);
        view.animateToCamera(camera, duration);
        break;

      case "animateToRegion":
        if(args == null) {
          break;
        }
        region = args.getMap(0);
        duration = args.getInt(1);
        lng = region.getDouble("longitude");
        lat = region.getDouble("latitude");
        lngDelta = region.getDouble("longitudeDelta");
        latDelta = region.getDouble("latitudeDelta");
        LatLngBounds bounds = new LatLngBounds(
            new LatLng(lat - latDelta / 2, lng - lngDelta / 2), // southwest
            new LatLng(lat + latDelta / 2, lng + lngDelta / 2)  // northeast
        );
        view.animateToRegion(bounds, duration);
        break;

      case "fitToElements":
        if(args == null) {
          break;
        }
        view.fitToElements(args.getMap(0), args.getBoolean(1));
        break;

      case "fitToSuppliedMarkers":
        if(args == null) {
          break;
        }
        view.fitToSuppliedMarkers(args.getArray(0), args.getMap(1), args.getBoolean(2));
        break;

      case "fitToCoordinates":
        if(args == null) {
          break;
        }
        view.fitToCoordinates(args.getArray(0), args.getMap(1), args.getBoolean(2));
        break;

      case "setMapBoundaries":
        if(args == null) {
          break;
        }
        view.setMapBoundaries(args.getMap(0), args.getMap(1));
        break;

      case "setIndoorActiveLevelIndex":
        if(args == null) {
          break;
        }
        view.setIndoorActiveLevelIndex(args.getInt(0));
        break;
    }
  }

  @Override
  @Nullable
  public Map getExportedCustomDirectEventTypeConstants() {
    Map<String, Map<String, String>> map = MapBuilder.of(
        "onMapReady", MapBuilder.of("registrationName", "onMapReady"),
        "onPress", MapBuilder.of("registrationName", "onPress"),
        "onLongPress", MapBuilder.of("registrationName", "onLongPress"),
        "onMarkerPress", MapBuilder.of("registrationName", "onMarkerPress"),
        "onMarkerSelect", MapBuilder.of("registrationName", "onMarkerSelect"),
        "onMarkerDeselect", MapBuilder.of("registrationName", "onMarkerDeselect"),
        "onCalloutPress", MapBuilder.of("registrationName", "onCalloutPress")
    );

    map.putAll(MapBuilder.of(
        "onUserLocationChange", MapBuilder.of("registrationName", "onUserLocationChange"),
        "onMarkerDragStart", MapBuilder.of("registrationName", "onMarkerDragStart"),
        "onMarkerDrag", MapBuilder.of("registrationName", "onMarkerDrag"),
        "onMarkerDragEnd", MapBuilder.of("registrationName", "onMarkerDragEnd"),
        "onPanDrag", MapBuilder.of("registrationName", "onPanDrag"),
        "onKmlReady", MapBuilder.of("registrationName", "onKmlReady"),
        "onPoiClick", MapBuilder.of("registrationName", "onPoiClick")
    ));

    map.putAll(MapBuilder.of(
        "onIndoorLevelActivated", MapBuilder.of("registrationName", "onIndoorLevelActivated"),
        "onIndoorBuildingFocused", MapBuilder.of("registrationName", "onIndoorBuildingFocused"),
        "onDoublePress", MapBuilder.of("registrationName", "onDoublePress"),
        "onMapLoaded", MapBuilder.of("registrationName", "onMapLoaded")
    ));

    return map;
  }

  @Override
  public LayoutShadowNode createShadowNodeInstance() {
    // A custom shadow node is needed in order to pass back the width/height of the map to the
    // view manager so that it can start applying camera moves with bounds.
    return new SizeReportingShadowNode();
  }

  @Override
  public void addView(MapView parent, View child, int index) {
    parent.addFeature(child, index);
  }

  @Override
  public int getChildCount(MapView view) {
    return view.getFeatureCount();
  }

  @Override
  public View getChildAt(MapView view, int index) {
    return view.getFeatureAt(index);
  }

  @Override
  public void removeViewAt(MapView parent, int index) {
    parent.removeFeatureAt(index);
  }

  @Override
  public void updateExtraData(MapView view, Object extraData) {
    view.updateExtraData(extraData);
  }

   */
  void pushEvent(ThemedReactContext context, View view, String name, WritableMap data) {
    context.getJSModule(RCTEventEmitter.class)
        .receiveEvent(view.getId(), name, data);
  }

  void pushEvent(ThemedReactContext context, Integer viewId, String name, WritableMap data) {
    context.getJSModule(RCTEventEmitter.class)
            .receiveEvent(viewId, name, data);
  }

  @Override
  public void onFirstLayout(View view, int left, int top, int right, int bottom) {
      if (initialBoundingBox != null && view instanceof MapView){
        ((MapView) view).zoomToBoundingBox(initialBoundingBox, false);
        initialBoundingBox = null;
      }
  }
}
