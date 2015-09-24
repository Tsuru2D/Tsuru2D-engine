package com.tsuru2d.engine.loader;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class ManagedAsset<T> implements Pool.Poolable {
    private final AssetLoader mLoader;
    private AssetID mAssetID;
    private T mAsset;
    private Class<T> mAssetClass;
    private int mReferenceCount;
    private Array<AssetObserver<T>> mObservers;

    /* package */ ManagedAsset(AssetLoader loader) {
        mLoader = loader;
    }

    public AssetID getAssetID() {
        return mAssetID;
    }

    /* package */ void setAssetID(AssetID assetID) {
        mAssetID = assetID;
    }

    /* package */ void setRawValue(T value, Class<T> valueCls) {
        mAsset = value;
        mAssetClass = valueCls;
    }

    /* package */ void invalidate() {
        mAsset = mLoader.getAssetRaw(mAssetID, mAssetClass);

        // TODO: This code will need to be changed if the
        // callback ever decides to register a new observer
        // (this can happen indirectly too)
        for (AssetObserver<T> observer : mObservers) {
            observer.onAssetUpdated(this);
        }
    }

    /* package */ int incrementRef() {
        return ++mReferenceCount;
    }

    /* package */ int decrementRef() {
        return --mReferenceCount;
    }

    /**
     * Add an observer for asset update events. This is useful
     * if you do not have control over the code which uses the
     * raw asset. Instead of calling {@link #get()} every time
     * the asset is used, you may cache the value and register
     * an observer instead.
     * <p>
     * Remember to call {@link #removeObserver(AssetObserver)}
     * after you are done using the asset, or it will not get
     * garbage collected!
     * @param observer The observer to register.
     */
    public void addObserver(AssetObserver<T> observer) {
        if (mObservers == null) {
            // Use an initial capacity of one, since in the typical
            // use case only one observer is required
            mObservers = new Array<AssetObserver<T>>(1);
        }

        if (!mObservers.contains(observer, true)) {
            mObservers.add(observer);
            incrementRef();
        }
    }

    /**
     * Remove a previously registered observer. Do this BEFORE
     * calling {@link AssetLoader#freeAsset(ManagedAsset)}.
     * @param observer The observer to unregister.
     */
    public void removeObserver(AssetObserver<T> observer) {
        if (mObservers != null) {
            if (mObservers.removeValue(observer, true)) {
                decrementRef();
            }
        }
    }

    /**
     * Get the raw asset wrapped by this class. Do NOT
     * hold a reference to the object returned by this method
     * for a prolonged time, since the underlying asset may
     * change during runtime! It is best to call this method
     * right before the asset is used, for example:
     * <p>
     * <ul>
     *     <li>Text: {@code drawString(asset.get(), x, y)}</li>
     *     <li>Sound: {@code asset.get().play()}</li>
     * </ul>
     */
    public T get() {
        return mAsset;
    }

    @Override
    public void reset() {
        if (mAsset instanceof Disposable) {
            ((Disposable)mAsset).dispose();
        }
        mAssetID = null;
        mAsset = null;
        mAssetClass = null;
    }
}
