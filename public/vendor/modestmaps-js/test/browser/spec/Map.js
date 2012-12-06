describe('Map', function() {
  var map, div, sink;

  function Receiver() { }
  Receiver.prototype.receive = function() { };

  beforeEach(function() {
    sink = new Receiver();
    div = document.createElement('div');
    div.id = +new Date();
    div.style.width = 500;
    div.style.height = 500;

    var template = 'http://{S}tile.openstreetmap.org/{Z}/{X}/{Y}.png';
    var subdomains = [ '', 'a.', 'b.', 'c.' ];
    var provider = new MM.TemplatedLayer(template, subdomains);

    map = new MM.Map(div, provider, new MM.Point(400, 400));
    map.setCenterZoom(new MM.Location(0, 0), 0);
  });

  it('attaches itself to a parent div', function() {
      expect(map.parent).toEqual(div);
  });

  describe('zoom restrictions and ranges', function() {

    it('has set a proper zoom level', function() {
        expect(map.getZoom()).toEqual(0);
    });

    it('can restrict its zoomlevel', function() {
        map.setZoomRange(5, 6);
        map.setZoom(7);
        expect(map.getZoom()).toEqual(6);
    });

  });

  it('has a center coordinate', function() {
      expect(typeof map.coordinate.row).toEqual('number');
      expect(typeof map.coordinate.column).toEqual('number');
      expect(typeof map.coordinate.zoom).toEqual('number');
  });

  describe('Navigation', function() {
      it('binds and calls drawn', function() {
          spyOn(sink, 'receive');
          map.addCallback('drawn', sink.receive);

          runs(function() {
              map.draw();
          });

          waits(500);

          runs(function() {
              expect(sink.receive).toHaveBeenCalledWith(map, undefined);
          });
      });

      it('binds and calls zoomed', function() {
          spyOn(sink, 'receive');
          map.addCallback('zoomed', sink.receive);

          runs(function() {
              map.zoomIn();
          });

          waits(500);

          runs(function() {
              expect(sink.receive).toHaveBeenCalledWith(map, 1);
          });
      });

      it('binds and calls panned', function() {
          spyOn(sink, 'receive');
          map.addCallback('panned', sink.receive);

          runs(function() {
              map.panBy(2, 2);
          });

          waits(500);

          runs(function() {
              expect(sink.receive).toHaveBeenCalledWith(map, [2, 2]);
          });
      });
  });

  describe('Layer Interface', function() {
      it('Can set a new layer at 0', function() {
          var p = new MM.TemplatedMapProvider(
              'http://{S}.tile.openstreetmap.org/{Z}/{X}/{Y}.png', ['a']);
          var l = new MM.Layer(p);
          map.setLayerAt(0, l);

          expect(map.getLayerAt(0)).toEqual(l);
      });
      it('Can insert a new layer at 0', function() {
          var p = new MM.TemplatedMapProvider(
              'http://{S}.tile.openstreetmap.org/{Z}/{X}/{Y}.png', ['a']);
          var l = new MM.Layer(p);

          expect(map.insertLayerAt(0, l)).toEqual(map);
          expect(map.getLayerAt(0)).toEqual(l);
          expect(map.getLayers().length).toEqual(2);
      });
      it('Can remove a new layer at 0', function() {
          var p = new MM.TemplatedMapProvider(
              'http://{S}.tile.openstreetmap.org/{Z}/{X}/{Y}.png', ['a']);
          var l = new MM.Layer(p);

          expect(map.insertLayerAt(0, l)).toEqual(map);

          expect(map.getLayerAt(0)).toEqual(l);
          expect(map.getLayers().length).toEqual(2);

          expect(map.removeLayerAt(0)).toEqual(map);
          expect(map.getLayers().length).toEqual(1);
      });

      it('Can swap a new layer at 0', function() {
          var p = new MM.TemplatedMapProvider(
              'http://{S}.tile.openstreetmap.org/{Z}/{X}/{Y}.png', ['a']);
          var l = new MM.Layer(p);

          var l1 = map.getLayerAt(0);

          expect(map.insertLayerAt(1, l)).toEqual(map);
          expect(map.swapLayersAt(0, 1)).toEqual(map);

          expect(map.getLayerAt(0)).toEqual(l);
          expect(map.getLayerAt(1)).toEqual(l1);
          expect(map.getLayers().length).toEqual(2);
      });

      it('Can remove a specific layer', function() {
          var p = new MM.TemplatedMapProvider(
              'http://{S}.tile.openstreetmap.org/{Z}/{X}/{Y}.png', ['a']);
          var l = new MM.Layer(p);

          expect(map.insertLayerAt(1, l)).toEqual(map);
          expect(map.removeLayer(l)).toEqual(map);

          expect(map.getLayers().length).toEqual(1);
      });
  });

  it('can transform an extent into a coord', function() {
      expect(map.extentCoordinate([
        { lat: -10, lon: -10 },
        { lat: 10, lon: 10 }])).toEqual(new MM.Coordinate(8, 8, 4));
  });

  it('binds and calls resized', function() {
      spyOn(sink, 'receive');
      map.addCallback('resized', sink.receive);

      runs(function() {
          map.setSize({
              x: 200, y: 300
          });
      });

      waits(500);

      runs(function() {
          expect(sink.receive).toHaveBeenCalledWith(map, new MM.Point(200, 300));
      });
  });

  it('can be cleanly destroyed', function() {
      map.destroy();
      expect(map.layers.length).toEqual(0);
  });
});
