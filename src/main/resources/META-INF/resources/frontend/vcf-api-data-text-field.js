import {ComboBox} from '@vaadin/combo-box';

class ApiDataTextField extends ComboBox {
    static get properties() {
        return {
            /** @private */
            _preventSelectOnClose: Boolean
        };
    }

    static get is() {
        return 'vcf-api-data-text-field';
    }

    constructor() {
        super();
    }

    /** @private */
    _onClick(e) {
        this._closeOnBlurIsPrevented = true;

        const path = e.composedPath();

        if (this._isClearButton(e)) {
            this._handleClearButtonClick(e);
        } else if (path.indexOf(this._toggleElement) > -1) {
            if (this.opened) {
                this._preventSelectOnClose = true;
                this.close();
            } else {
                this.open();
            }
        } else {
            this._onHostClick(e);
        }

        this._closeOnBlurIsPrevented = false;
    }

    /** @private */
    _commitValue() {
        const items = this._getOverlayItems();
        if (items && this._focusedIndex > -1 && !this._preventSelectOnClose) {
            const focusedItem = items[this._focusedIndex];
            if (this.selectedItem !== focusedItem) {
                this.selectedItem = focusedItem;
            }
            // Make sure input field is updated in case value doesn't change (i.e. FOO -> foo)
            this._inputElementValue = this._getItemLabel(this.selectedItem);
        } else if (this._inputElementValue === '' || this._inputElementValue === undefined) {
            this.selectedItem = null;

            if (this.allowCustomValue) {
                this.value = '';
            }
        } else {
            const toLowerCase = (item) => item && item.toLowerCase && item.toLowerCase();

            // Try to find an item whose label matches the input value. A matching item is searched from
            // the filteredItems array (if available) and the selectedItem (if available).
            const itemMatchingByLabel = [...(this.filteredItems || []), this.selectedItem].find((item) => {
                return toLowerCase(this._getItemLabel(item)) === toLowerCase(this._inputElementValue);
            });

            if (
                this.allowCustomValue &&
                // To prevent a repetitive input value being saved after pressing ESC and Tab.
                !itemMatchingByLabel
            ) {
                const customValue = this._inputElementValue;

                // Store reference to the last custom value for checking it on focusout.
                this._lastCustomValue = customValue;

                // An item matching by label was not found, but custom values are allowed.
                // Dispatch a custom-value-set event with the input value.
                const e = new CustomEvent('custom-value-set', {
                    detail: customValue,
                    composed: true,
                    cancelable: true,
                    bubbles: true,
                });
                this.dispatchEvent(e);
                if (!e.defaultPrevented) {
                    this._selectItemForValue(customValue);
                    this.value = customValue;
                }
            } else {
                // Revert the input value
                this._inputElementValue = this.selectedItem ? this._getItemLabel(this.selectedItem) : this.value || '';
                this._focusedIndex = 0;
            }
        }

        this._preventSelectOnClose = false;

        this._detectAndDispatchChange();

        this._clearSelectionRange();

        if (!this.dataProvider) {
            this.filter = '';
        }
    }

    /** @private */
    _loadingChanged(loading) {
        if (loading) {
            this._focusedIndex = -1;
        } else {
            this._focusFirst();
        }
    }

    /** @private */
    _selectedItemChanged(selectedItem) {
        if (selectedItem === null || selectedItem === undefined) {
            if (this.filteredItems) {
                if (!this.allowCustomValue) {
                    this.value = '';
                }

                this._toggleHasValue(this.value !== '');
                this._inputElementValue = this.value;
            }
        } else {
            const value = this._getItemValue(selectedItem);
            if (this.value !== value) {
                this.value = value;
                if (this.value !== value) {
                    // The value was changed to something else in value-changed listener,
                    // so prevent from resetting it to the previous value.
                    return;
                }
            }

            this._toggleHasValue(true);
            this._inputElementValue = this._getItemLabel(selectedItem);
        }

        this.$.dropdown._selectedItem = selectedItem;
        const items = this._getOverlayItems();
        if (this.filteredItems && items) {
            if (selectedItem == null) {
                this._focusedIndex = 0;
            } else {
                this._focusedIndex = this.filteredItems.indexOf(selectedItem);
            }
        }
    }

    /** @private */
    _onFocusout(event) {
        // Fixes the problem with `focusout` happening when clicking on the scroll bar on Edge
        if (event.relatedTarget === this.$.dropdown.$.overlay) {
            event.composedPath()[0].focus();
            return;
        }

        if(this._focusedIndex > -1 && this._getOverlayItems().indexOf(this.selectedItem) != this._focusedIndex){
            this._focusedIndex = -1;
        }

        if (!this.readonly && !this._closeOnBlurIsPrevented) {
            // User's logic in `custom-value-set` event listener might cause input to blur,
            // which will result in attempting to commit the same custom value once again.
            if (!this.opened && this.allowCustomValue && this._inputElementValue === this._lastCustomValue) {
                delete this._lastCustomValue;
                return;
            }

            this._closeOrCommit();
            this._focusFirst();
        }
    }

    /** @private */
    _focusFirst() {
        if (this.filteredItems && this.filteredItems.length > 0) {
            if (this.selectedItem) {
                this._focusedIndex = this._inputElementValue.length == this.selectedItem.label.length ? this._getOverlayItems().indexOf(this.selectedItem) : 0;
            } else {
                this._focusedIndex = 0;
            }
        } else if (this.selectedItem && this._inputElementValue && (this.filter.length > 0 !== this._inputElementValue.length == 0)) {
            this._focusedIndex = 0;
        }
    }
}

customElements.define(ApiDataTextField.is, ApiDataTextField);

export {ApiDataTextField};