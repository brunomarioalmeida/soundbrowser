
package com.soundbrowser.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class SourceRoot {

    @Expose
    private Item item;

    /**
     * 
     * @return
     *     The item
     */
    public Item getItem() {
        return item;
    }

    /**
     * 
     * @param item
     *     The item
     */
    public void setItem(Item item) {
        this.item = item;
    }

}
