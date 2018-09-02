package com.kasao.qintaiframework.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.kasao.qintaiframework.until.LogUtil;


public abstract class HeaderFooterRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final int VIEW_TYPE_MAX_COUNT = 10000000;
    private static final int HEADER_VIEW_TYPE_OFFSET = 0;
    private static final int FOOTER_VIEW_TYPE_OFFSET = HEADER_VIEW_TYPE_OFFSET + VIEW_TYPE_MAX_COUNT;
    private static final int CONTENT_VIEW_TYPE_OFFSET = FOOTER_VIEW_TYPE_OFFSET + VIEW_TYPE_MAX_COUNT;

    private int headerItemCount;
    private int contentItemCount;
    private int footerItemCount;

    /**
     * {@inheritDoc}
     */
    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {

        // Delegate to proper methods based on the viewType ranges.
        if (viewType >= HEADER_VIEW_TYPE_OFFSET && viewType < HEADER_VIEW_TYPE_OFFSET + VIEW_TYPE_MAX_COUNT) {
            return onCreateHeaderItemViewHolder(parent, viewType - HEADER_VIEW_TYPE_OFFSET);
        } else if (viewType >= FOOTER_VIEW_TYPE_OFFSET && viewType < FOOTER_VIEW_TYPE_OFFSET + VIEW_TYPE_MAX_COUNT) {
            return onCreateFooterItemViewHolder(parent, viewType - FOOTER_VIEW_TYPE_OFFSET);
        } else if (viewType >= CONTENT_VIEW_TYPE_OFFSET && viewType < CONTENT_VIEW_TYPE_OFFSET + VIEW_TYPE_MAX_COUNT) {
            return onCreateContentItemViewHolder(parent, viewType - CONTENT_VIEW_TYPE_OFFSET);
        } else {
            // This shouldn't happen as we check that the viewType provided by the client is valid.
            if (MyApplication.Companion.isDebug()) {
                throw new IllegalStateException("This shouldn't happen as we check that the viewType provided by the client is valid.");
            } else {
                LogUtil.e("headerFooterRecycleViewAdapter","This shouldn't happen as we check that the viewType provided by the client is valid.");
            }

            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onBindViewHolder(VH viewHolder, int position) {
        // Delegate to proper methods based on the viewType ranges.
        if (headerItemCount > 0 && position < headerItemCount) {
            onBindHeaderItemViewHolder(viewHolder, position);
        } else if (contentItemCount > 0 && position - headerItemCount < contentItemCount) {
            onBindContentItemViewHolder(viewHolder, position - headerItemCount);
        } else {
            onBindFooterItemViewHolder(viewHolder, position - headerItemCount - contentItemCount);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getItemCount() {
        // Cache the counts and return the sum of them.
        headerItemCount = getHeaderItemCount();
        contentItemCount = getContentItemCount();
        footerItemCount = getFooterItemCount();
        return headerItemCount + contentItemCount + footerItemCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getItemViewType(int position) {
        // Delegate to proper methods based on the position, but validate first.
        if (headerItemCount > 0 && position < headerItemCount) {
            return validateViewType(getHeaderItemViewType(position)) + HEADER_VIEW_TYPE_OFFSET;
        } else if (contentItemCount > 0 && position - headerItemCount < contentItemCount) {
            return validateViewType(getContentItemViewType(position - headerItemCount)) + CONTENT_VIEW_TYPE_OFFSET;
        } else {
            return validateViewType(getFooterItemViewType(position - headerItemCount - contentItemCount)) + FOOTER_VIEW_TYPE_OFFSET;
        }
    }

    /**
     * Validates that the view type is within the valid range.
     *
     * @param viewType the view type.
     * @return the given view type.
     */
    private int validateViewType(int viewType) {
        if (viewType < 0 || viewType >= VIEW_TYPE_MAX_COUNT) {
            String msg = "viewType must be between 0 and " + VIEW_TYPE_MAX_COUNT;
            if (MyApplication.Companion.isDebug()) {
                throw new IllegalStateException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
        }
        return viewType;
    }

    /**
     * Notifies that a header item is inserted.
     *
     * @param position the position of the header item.
     */
    public final void notifyHeaderItemInserted(int position) {
        getItemCount();
        int newHeaderItemCount = getHeaderItemCount();
        if (position < 0 || position >= newHeaderItemCount) {
            String msg = "The given position " + position + " is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemInserted(position);
        } catch (Throwable e) {
            LogUtil.e("Tag",e.getMessage());
        }
    }

    /**
     * Notifies that multiple header items are inserted.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyHeaderItemRangeInserted(int positionStart, int itemCount) {
        getItemCount();
        int newHeaderItemCount = getHeaderItemCount();
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > newHeaderItemCount) {
            String msg = "The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRangeInserted(positionStart, itemCount);
        } catch (Throwable e) {
            LogUtil.e("Tag",e.getMessage());
        }
    }

    /**
     * Notifies that a header item is changed.
     *
     * @param position the position.
     */
    public final void notifyHeaderItemChanged(int position) {
        if (position < 0 || position >= headerItemCount) {
            getItemCount();
            if (position < 0 || position >= headerItemCount) {
                String msg = "The given position " + position + " is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
                if (MyApplication.Companion.isDebug()) {
                    throw new IndexOutOfBoundsException(msg);
                } else {
                    LogUtil.e("Tag",msg);
                }
                return;
            }
        }
        try {
            notifyItemChanged(position);
        } catch (Throwable e) {
            LogUtil.e("Tag",e.getMessage());
        }
    }

    /**
     * Notifies that multiple header items are changed.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyHeaderItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > headerItemCount) {
            String msg = "The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRangeChanged(positionStart, itemCount);
        } catch (Throwable e) {
            LogUtil.e("tag",e.getMessage());
        }
    }


    /**
     * Notifies that an existing header item is moved to another position.
     *
     * @param fromPosition the original position.
     * @param toPosition   the new position.
     */
    public void notifyHeaderItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= headerItemCount || toPosition >= headerItemCount) {
            String msg = "The given fromPosition " + fromPosition + " or toPosition " + toPosition + " is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("tag",msg);
            }
            return;
        }
        try {
            notifyItemMoved(fromPosition, toPosition);
        } catch (Throwable e) {
            LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that a header item is removed.
     *
     * @param position the position.
     */
    public void notifyHeaderItemRemoved(int position) {
        getItemCount();
        if (position < 0 || position >= headerItemCount) {
            String msg = "The given position " + position + " is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRemoved(position);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that multiple header items are removed.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public void notifyHeaderItemRangeRemoved(int positionStart, int itemCount) {
        getItemCount();
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > headerItemCount) {
            String msg = "The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRangeRemoved(positionStart, itemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that a content item is inserted.
     *
     * @param position the position of the content item.
     */
    public final void notifyContentItemInserted(int position) {
        getItemCount();
        int newHeaderItemCount = getHeaderItemCount();
        int newContentItemCount = getContentItemCount();
        if (position < 0 || position >= newContentItemCount) {
            String msg = "The given position " + position + " is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemInserted(position + newHeaderItemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that multiple content items are inserted.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyContentItemRangeInserted(int positionStart, int itemCount) {
        getItemCount();
        int newHeaderItemCount = getHeaderItemCount();
        int newContentItemCount = getContentItemCount();
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > newContentItemCount) {
            String msg = "The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRangeInserted(positionStart + newHeaderItemCount, itemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that a content item is changed.
     *
     * @param position the position.
     */
    public final void notifyContentItemChanged(int position) {
        if (contentItemCount != getContentItemCount()) {
            getItemCount();
        }
        if (position < 0 || position >= contentItemCount) {
            String msg = "The given position " + position + " is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemChanged(position + headerItemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that multiple content items are changed.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyContentItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > contentItemCount) {
            String msg = "The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRangeChanged(positionStart + headerItemCount, itemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that an existing content item is moved to another position.
     *
     * @param fromPosition the original position.
     * @param toPosition   the new position.
     */
    public final void notifyContentItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= contentItemCount || toPosition >= contentItemCount) {

            String msg = "The given fromPosition " + fromPosition + " or toPosition " + toPosition + " is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemMoved(fromPosition + headerItemCount, toPosition + headerItemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that a content item is removed.
     *
     * @param position the position.
     */
    public final void notifyContentItemRemoved(int position) {
        if (position < 0 || position >= contentItemCount) {
            String msg = "The given position " + position + " is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRemoved(position + headerItemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }

    }

    /**
     * Notifies that multiple content items are removed.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyContentItemRangeRemoved(int positionStart, int itemCount) {
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > contentItemCount) {
            String msg = "The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for content items [0 - " + (contentItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRangeRemoved(positionStart + headerItemCount, itemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that a footer item is inserted.
     *
     * @param position the position of the content item.
     */
    public final void notifyFooterItemInserted(int position) {
        getItemCount();
        int newHeaderItemCount = getHeaderItemCount();
        int newContentItemCount = getContentItemCount();
        int newFooterItemCount = getFooterItemCount();
        if (position < 0 || position >= newFooterItemCount) {
            String msg = "The given position " + position + " is not within the position bounds for footer items [0 - " + (newFooterItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemInserted(position + newHeaderItemCount + newContentItemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that multiple footer items are inserted.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyFooterItemRangeInserted(int positionStart, int itemCount) {
        getItemCount();
        int newHeaderItemCount = getHeaderItemCount();
        int newContentItemCount = getContentItemCount();
        int newFooterItemCount = getFooterItemCount();
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > newFooterItemCount) {
            String msg = "The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for footer items [0 - " + (newFooterItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRangeInserted(positionStart + newHeaderItemCount + newContentItemCount, itemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that a footer item is changed.
     *
     * @param position the position.
     */
    public final void notifyFooterItemChanged(int position) {
        if (position < 0 || position >= footerItemCount) {
            String msg = "The given position " + position + " is not within the position bounds for footer items [0 - " + (footerItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemChanged(position + headerItemCount + contentItemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that multiple footer items are changed.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyFooterItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > footerItemCount) {
            String msg = "The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for footer items [0 - " + (footerItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRangeChanged(positionStart + headerItemCount + contentItemCount, itemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that an existing footer item is moved to another position.
     *
     * @param fromPosition the original position.
     * @param toPosition   the new position.
     */
    public final void notifyFooterItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= footerItemCount || toPosition >= footerItemCount) {
            String msg = "The given fromPosition " + fromPosition + " or toPosition " + toPosition + " is not within the position bounds for footer items [0 - " + (footerItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemMoved(fromPosition + headerItemCount + contentItemCount, toPosition + headerItemCount + contentItemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that a footer item is removed.
     *
     * @param position the position.
     */
    public final void notifyFooterItemRemoved(int position) {
        if (position < 0 || position >= footerItemCount) {
            String msg = "The given position " + position + " is not within the position bounds for footer items [0 - " + (footerItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRemoved(position + headerItemCount + contentItemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Notifies that multiple footer items are removed.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyFooterItemRangeRemoved(int positionStart, int itemCount) {
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > footerItemCount) {
            String msg = "The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for footer items [0 - " + (footerItemCount - 1) + "].";
            if (MyApplication.Companion.isDebug()) {
                throw new IndexOutOfBoundsException(msg);
            } else {
                LogUtil.e("Tag",msg);
            }
            return;
        }
        try {
            notifyItemRangeRemoved(positionStart + headerItemCount + contentItemCount, itemCount);
        } catch (Throwable e) {
             LogUtil.e("tag",e.getMessage());
        }
    }

    /**
     * Gets the header item view type. By default, this method returns 0.
     *
     * @param position the position.
     * @return the header item view type (within the range [0 - VIEW_TYPE_MAX_COUNT-1]).
     */
    protected int getHeaderItemViewType(int position) {
        return 0;
    }

    /**
     * Gets the footer item view type. By default, this method returns 0.
     *
     * @param position the position.
     * @return the footer item view type (within the range [0 - VIEW_TYPE_MAX_COUNT-1]).
     */
    protected int getFooterItemViewType(int position) {
        return 0;
    }

    /**
     * Gets the content item view type. By default, this method returns 0.
     *
     * @param position the position.
     * @return the content item view type (within the range [0 - VIEW_TYPE_MAX_COUNT-1]).
     */
    protected int getContentItemViewType(int position) {
        return 0;
    }

    /**
     * Gets the header item count. This method can be called several times, so it should not calculate the count every time.
     *
     * @return the header item count.
     */
    protected abstract int getHeaderItemCount();

    /**
     * Gets the footer item count. This method can be called several times, so it should not calculate the count every time.
     *
     * @return the footer item count.
     */
    protected abstract int getFooterItemCount();

    /**
     * Gets the content item count. This method can be called several times, so it should not calculate the count every time.
     *
     * @return the content item count.
     */
    protected abstract int getContentItemCount();

    /**
     * This method works exactly the same as {@link #onCreateViewHolder(ViewGroup, int)}, but for header items.
     *
     * @param parent         the parent view.
     * @param headerViewType the view type for the header.
     * @return the view holder.
     */
    protected abstract VH onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType);

    /**
     * This method works exactly the same as {@link #onCreateViewHolder(ViewGroup, int)}, but for footer items.
     *
     * @param parent         the parent view.
     * @param footerViewType the view type for the footer.
     * @return the view holder.
     */
    protected abstract VH onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType);

    /**
     * This method works exactly the same as {@link #onCreateViewHolder(ViewGroup, int)}, but for content items.
     *
     * @param parent          the parent view.
     * @param contentViewType the view type for the content.
     * @return the view holder.
     */
    protected abstract VH onCreateContentItemViewHolder(ViewGroup parent, int contentViewType);

    /**
     * This method works exactly the same as {@link #onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int)}, but for header items.
     *
     * @param headerViewHolder the view holder for the header item.
     * @param position         the position.
     */
    protected abstract void onBindHeaderItemViewHolder(VH headerViewHolder, int position);

    /**
     * This method works exactly the same as {@link #onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int)}, but for footer items.
     *
     * @param footerViewHolder the view holder for the footer item.
     * @param position         the position.
     */
    protected abstract void onBindFooterItemViewHolder(VH footerViewHolder, int position);

    /**
     * This method works exactly the same as {@link #onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int)}, but for content items.
     *
     * @param contentViewHolder the view holder for the content item.
     * @param position          the position.
     */
    protected abstract void onBindContentItemViewHolder(VH contentViewHolder, int position);


}