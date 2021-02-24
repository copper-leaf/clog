package clog.dsl

import clog.Clog
import clog.ClogProfile
import clog.impl.DefaultTagProvider
import clog.test.impl.TestLogger
import clog.test.impl.clogTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ClogConfigurationDslTest {

    @Test
    fun testUpdateProfile() {
        val originalProfile = Clog.getInstance()
        val newProfile = ClogProfile()

        var profileBeingUpdated: ClogProfile? = null
        var newProfileAfterUpdate: ClogProfile? = null
        Clog.updateProfile {
            profileBeingUpdated = it
            newProfileAfterUpdate = newProfile

            newProfile
        }

        assertSame(originalProfile, profileBeingUpdated)
        assertSame(newProfile, newProfileAfterUpdate)
        assertSame(newProfile, Clog.getInstance())

        Clog.setInstance(originalProfile)
        assertSame(originalProfile, Clog.getInstance())
    }

    @Test
    fun testTag() {
        clogTest {
            val newProfile = Clog.tag("test tag")
            val globalProfile = Clog.getInstance()

            // Clog.tag does not change the global profile
            assertNotSame(globalProfile, newProfile)
            assertEquals(globalProfile.tagProvider, DefaultTagProvider())
            assertNotEquals(globalProfile.tagProvider, DefaultTagProvider("test tag"))

            // Clog.tag returns a new profile with the custom tag, which has the same API as the top-level Clog object
            assertEquals(newProfile.tagProvider, DefaultTagProvider("test tag"))
        }
    }

    @Test
    fun testMaybeTag_withTag() {
        clogTest {
            val newProfile = Clog.maybeTag("test tag")
            val globalProfile = Clog.getInstance()

            // Clog.maybeTag with a tag is the same as Clog.tag
            assertNotSame(globalProfile, newProfile)
            assertEquals(globalProfile.tagProvider, DefaultTagProvider())
            assertNotEquals(globalProfile.tagProvider, DefaultTagProvider("test tag"))

            assertEquals(newProfile.tagProvider, DefaultTagProvider("test tag"))
        }
    }

    @Test
    fun testMaybeTag_withNull() {
        clogTest {
            val newProfile = Clog.maybeTag(null)
            val globalProfile = Clog.getInstance()

            // Clog.maybeTag with no tag is the same as Clog.getInstance()
            assertSame(globalProfile, newProfile)
            assertEquals(globalProfile.tagProvider, DefaultTagProvider())
        }
    }

    @Test
    fun testLogInProductionFilter_notProd() {
        clogTest { logger ->
            val isDebug = true
            Clog.configureLoggingInProduction(isDebug)

            Clog.v("m1")
            assertTrue(logger.messageWasLogged)

            Clog.e("m2")
            assertTrue(logger.messageWasLogged)
        }
    }

    @Test
    fun testLogInProductionFilter_prod() {
        clogTest { logger ->
            val isDebug = false
            Clog.configureLoggingInProduction(isDebug)

            Clog.v("m1")
            assertFalse(logger.messageWasLogged)

            Clog.e("m2")
            assertFalse(logger.messageWasLogged)
        }
    }

    @Test
    fun testAddLogger() {
        clogTest { logger ->
            val logger1 = TestLogger()
            val logger2 = TestLogger()

            Clog.addLogger(logger1)
            Clog.addLogger(logger2)

            Clog.v("m1")
            assertTrue(logger.messageWasLogged)
            assertTrue(logger1.messageWasLogged)
            assertTrue(logger2.messageWasLogged)

            Clog.e("m2")
            assertTrue(logger.messageWasLogged)
            assertTrue(logger1.messageWasLogged)
            assertTrue(logger2.messageWasLogged)
        }
    }

    @Test
    fun testTagWhitelisting() {
        clogTest { logger ->
            Clog.addTagToWhitelist("t2")

            Clog.tag("t1").v("m1")
            assertFalse(logger.messageWasLogged)

            Clog.tag("t2").v("m2")
            assertTrue(logger.messageWasLogged)
            assertEquals("t2", logger.lastMessageTag)
            assertEquals("m2", logger.lastMessage)
        }
    }

    @Test
    fun testTagBlacklisting() {
        clogTest { logger ->
            Clog.addTagToBlacklist("t1")

            Clog.tag("t1").v("m1")
            assertFalse(logger.messageWasLogged)

            Clog.tag("t2").v("m2")
            assertTrue(logger.messageWasLogged)
            assertEquals("t2", logger.lastMessageTag)
            assertEquals("m2", logger.lastMessage)
        }
    }

    @Test
    fun testMinPriorityFilter() {
        clogTest { logger ->
            Clog.setMinPriority(Clog.Priority.ERROR)

            Clog.v("m1")
            assertFalse(logger.messageWasLogged)

            Clog.e("m2")
            assertTrue(logger.messageWasLogged)
            assertEquals("m2", logger.lastMessage)
        }
    }
}
