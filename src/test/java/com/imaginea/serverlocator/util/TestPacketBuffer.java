package com.imaginea.serverlocator.util;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class TestPacketBuffer {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testValidateInt() {
		Assert.assertEquals(PacketBuffer.validateInt(new byte[]{57,48}), true);
		Assert.assertEquals(PacketBuffer.validateInt(new byte[]{52}),true);
		Assert.assertFalse(PacketBuffer.validateInt(new byte[]{58,48}));
		Assert.assertFalse(PacketBuffer.validateInt(new byte[]{56,99}));
		Assert.assertFalse(PacketBuffer.validateInt(new byte[]{52,(byte)'.'}));		
	}

	@Test
	public void testReadInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadString() {
		fail("Not yet implemented");
	}

	@Test
	public void testSkipByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testSkipString() {
		fail("Not yet implemented");
	}

}
