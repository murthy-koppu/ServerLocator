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
	public void testValidateIntPass() {
		PacketBuffer packetBuffer = new PacketBuffer(new byte[]{91,24});
		Assert.assertEquals(packetBuffer.validateInt(new byte[]{52,49}),true);
		
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
