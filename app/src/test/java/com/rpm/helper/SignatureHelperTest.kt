package com.rpm.helper

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_SIGNATURES
import android.content.pm.Signature
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SignatureHelperTest {

    @Spy lateinit var packageInfo: PackageInfo

    @Mock lateinit var context: Context
    @Mock lateinit var signature: Signature
    @Mock lateinit var packageManager: PackageManager

    lateinit var mSubject: SignatureHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        packageInfo.signatures = arrayOf(signature)

        `when`(packageManager.getPackageInfo(anyString(), anyInt())).thenReturn(packageInfo)
        `when`(context.packageName).thenReturn("com.rpm")
        `when`(context.packageManager).thenReturn(packageManager)
        `when`(signature.toCharsString())
            .thenReturn("3082030d308201f5a00302010202044ea11039300d06092a864886f70d01010b05003037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f6964204465627567301e170d3135303332353132343831385a170d3435303331373132343831385a3037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f696420446562756730820122300d06092a864886f70d01010105000382010f003082010a028201010092af0de57b174af100687623a87e056f8431f573554680cbd403dafa52ae9251b9263205406d44fe882b7eac21a53237adbb8836c3ae303428732a086b0c58d3c1cbc62c5c039dfda5c815e6bca2cbb94d128a675ea2fac3e0b8b5c02a907f513182e6b3ad79c18ffc520209dda4dc4427e2f66fcd63fd527ef1cf6ed93a59e460393b2d5437fcbce59e4951251691f717577827005e62465b51ccd240e2ed5dbeba9b6c0d319af4598919ecd1bbbc5882660e718b92d3d0de1a99d58ce642a0aa77d4cc9ab72c054ccabcae252da3e03063851c7c633d91888e9121984224105126fdea039dda38dc63d4ffa38946f7c403655df552122fc8139fe5a3a3e84b0203010001a321301f301d0603551d0e04160414f0e6f9c28a4d9bbf2d9ac8314640c1af5e65204c300d06092a864886f70d01010b0500038201010022ceff7975fb5c7ddd89ca947bd8ecb455b76297344f375b253fa306151d6a991c92cd80301d8d78a1d6a6742f102deb52496cd94fd1581396c87dffffade6a17d7f6f2f3b6f28f3d2c1eebf16c1333dd2a1115f3f413dce4fe67eebd0948a886d7f235e4996c08d63ab9fafc9b2aaeb3d36bc5bdb03e1d6262fdd4bf97032f07efa752bd7b7723cb41995d5a85b24e96781c7e64deeac75511e1972b2861f775042e10aecae764e984eeb7f54f9f52939906be037b6d2909215ad0157f80d340421f8670248fcb2f1e95e0e15778b6374a787db3a3617291b59b59756797391d8a0e306bd3d0cf2c9e4120da55c50a87c10dd4ed2a1470cf992c657bb7404bb")

        mSubject = SignatureHelper(context = context)
    }

    @Test
    fun getAppSignatures() {
        val result = mSubject.getAppSignatures()

        assertTrue(result.size == 1)
        assertEquals("JwmXjkMlh6d", result[0])
    }
}
