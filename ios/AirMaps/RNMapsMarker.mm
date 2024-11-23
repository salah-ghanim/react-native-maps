//
//  RNMapsMarker.m
//  AirMaps
//
//  Created by Salah Ghanim on 23.11.24.
//  Copyright © 2024 react-native-maps. All rights reserved.
//

#import "RNMapsMarker.h"
#import "AIRMapMarker.h"

#import <react/renderer/components/RNMapsSpecs/ComponentDescriptors.h>
#import <react/renderer/components/RNMapsSpecs/EventEmitters.h>
#import <react/renderer/components/RNMapsSpecs/Props.h>
#import <react/renderer/components/RNMapsSpecs/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"
#import <React/RCTConversions.h>

using namespace facebook::react;

@interface RNMapsMarker () <RCTComponentViewProtocol>
@end

@implementation RNMapsMarker {
    AIRMapMarker *_view;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<RNMapsMarkerViewComponentDescriptor>();
}


- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const RNMapsMarkerViewProps>();
    _props = defaultProps;

      AIRMapMarker *marker = [AIRMapMarker new];
      [marker addTapGestureRecognizer];
      marker.isAccessibilityElement = YES;
      marker.accessibilityElementsHidden = NO;
      marker.backgroundColor = RCTUIColorFromSharedColor(defaultProps->backgroundColor);
      marker.coordinate = CLLocationCoordinate2DMake(defaultProps->coordinate.latitude, defaultProps->coordinate.longitude);
      

    _view = marker;

    self.contentView = _view;
  }

  return self;
}

- (AIRMapMarker *) marker {
    return _view;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
  const auto &oldViewProps = *std::static_pointer_cast<RNMapsMarkerViewProps const>(_props);
  const auto &newViewProps = *std::static_pointer_cast<RNMapsMarkerViewProps const>(props);

  if (oldViewProps.identifier != newViewProps.identifier) {
    _view.identifier = [[NSString alloc] initWithCString:newViewProps.identifier.c_str() encoding:NSASCIIStringEncoding];
  }
    if (oldViewProps.backgroundColor != newViewProps.backgroundColor){
        _view.backgroundColor = RCTUIColorFromSharedColor(newViewProps.backgroundColor);
    }
    if (oldViewProps.coordinate.latitude != newViewProps.coordinate.latitude || oldViewProps.coordinate.longitude != newViewProps.coordinate.latitude){
        _view.coordinate = CLLocationCoordinate2DMake(newViewProps.coordinate.latitude, newViewProps.coordinate.longitude);
    }

  [super updateProps:props oldProps:oldProps];
}

@end

Class<RCTComponentViewProtocol> RNMapsMarkerViewCls(void)
{
  return RNMapsMarker.class;
}
