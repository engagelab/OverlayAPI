var FeatureDetails = Backbone.View.extend({
  initialize: function (args) {
    dispatch.on(this.model.cid + ':marker:toggle', this.toggle, this);
  },

  events: {
    'click .back': 'collapse'
  },

  render: function () {
    this.setElement(Handlebars.templates.details(this.model.toJSON()));
    return this;
  },

  toggle: function() {
    if (!this.$el.hasClass('expand')) {
      this.expand();
    } else {
      this.collapse();
    }
  },

  expand: function() {
    this.$el.addClass('expand')
      .animate({'right': 0});
    dispatch.trigger(this.model.cid + ':details:expand');
  },

  collapse: function() {
    this.$el.animate({'right': -this.$el.width()}, function() {
      $(this).removeClass('expand');
    });
    dispatch.trigger(this.model.cid + ':details:collapse');
  }
});
