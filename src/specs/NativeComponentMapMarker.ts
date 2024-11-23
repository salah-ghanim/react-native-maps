import type {HostComponent, ViewProps} from 'react-native';

import type {Double} from 'react-native/Libraries/Types/CodegenTypes';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';

type LatLng = Readonly<{
  latitude: Double;
  longitude: Double;
}>;

type Point = Readonly<{
  x: Double;
  y: Double;
}>;

interface NativeProps extends ViewProps {
  anchor?: Point;

  /**
   * Specifies the point in the marker image at which to anchor the callout when it is displayed.
   * This is specified in the same coordinate system as the anchor.
   *
   * See the `anchor` prop for more details.
   *
   * @default {x: 0.5, y: 0.0}
   * @platform iOS: Google Maps only. For Apple Maps, see the `calloutOffset` prop
   * @platform Android: Supported
   */
  calloutAnchor?: Point;

  /**
   * The offset (in points) at which to place the callout bubble.
   * When this property is set to (0, 0),
   * the anchor point of the callout bubble is placed on the top-center point of the marker view’s frame.
   *
   * Specifying positive offset values moves the callout bubble down and to the right,
   * while specifying negative values moves it up and to the left
   *
   * @default {x: 0.0, y: 0.0}
   * @platform iOS: Apple Maps only. For Google Maps, see the `calloutAnchor` prop
   * @platform Android: Not supported. See see the `calloutAnchor` prop
   */
  calloutOffset?: Point;

  /**
   * The offset (in points) at which to display the annotation view.
   *
   * By default, the center point of an annotation view is placed at the coordinate point of the associated annotation.
   *
   * Positive offset values move the annotation view down and to the right, while negative values move it up and to the left.
   *
   * @default {x: 0.0, y: 0.0}
   * @platform iOS: Apple Maps only. For Google Maps, see the `anchor` prop
   * @platform Android: Not supported. See see the `anchor` prop
   */
  centerOffset?: Point;

  /**
   * The coordinate for the marker.
   *
   * @platform iOS: Supported
   * @platform Android: Supported
   */
  coordinate: LatLng;

  /**
   * The description of the marker.
   *
   * This is only used if the <Marker /> component has no children that are a `<Callout />`,
   * in which case the default callout behavior will be used,
   * which will show both the `title` and the `description`, if provided.
   *
   * @platform iOS: Supported
   * @platform Android: Supported
   */
  description?: string;

  /**
   * if `true` allows the marker to be draggable (re-positioned).
   *
   * @default false
   * @platform iOS: Supported
   * @platform Android: Supported
   */
  draggable?: boolean;

  /**
   * Sets whether this marker should be flat against the map true or a billboard facing the camera.
   *
   * @default false
   * @platform iOS: Google Maps only
   * @platform Android: Supported
   */
  flat?: boolean;

  /**
   * A string that can be used to identify this marker.
   *
   * @platform iOS: Supported
   * @platform Android: Supported
   */
  identifier?: string;
}

export default codegenNativeComponent<NativeProps>('RNMapsMarkerView', {
  excludedPlatforms: ['android'],
}) as HostComponent<NativeProps>;
