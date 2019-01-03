package com.like.thirdpartyloginandshare.share.params.text

import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * @param text 文本。长度需大于0且不超过10KB
 */
data class WxTextParams(val text: String) : ShareParams