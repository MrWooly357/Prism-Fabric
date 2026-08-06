package net.mrwooly357.prism.client.render.item.model

interface PrismItemModel {

    var prism_shouldReceiveCustomData: Boolean
        get() {
            throw AssertionError("Implemented in Mixin.")
        }
        set(_) {
            throw AssertionError("Implemented in Mixin.")
        }
}
