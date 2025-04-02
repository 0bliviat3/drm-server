package com.core.drm.crypto.domain;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;

/*
 * cipher 객체의 초기화 여부 포함하기 위해 상속함
 */
public class CipherWrapper extends PaddedBufferedBlockCipher {

    private boolean initFlag;

    public CipherWrapper(BlockCipher blockCipher, BlockCipherPadding blockCipherPadding) {
        super(blockCipher, blockCipherPadding);
    }

    public CipherWrapper(BlockCipher blockCipher) {
        super(blockCipher);
    }

    public boolean isInitCipher() {
        return this.initFlag;
    }

    @Override
    public void init(boolean b, CipherParameters cipherParameters) throws IllegalArgumentException {
        super.init(b, cipherParameters);
        this.initFlag = true;
    }

    @Override
    public void reset() {
        super.reset();
        this.initFlag = false;
    }
}
