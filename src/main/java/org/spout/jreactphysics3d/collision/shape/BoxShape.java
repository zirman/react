/*
 * This file is part of JReactPhysics3D.
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 * JReactPhysics3D is licensed under the Spout License Version 1.
 *
 * JReactPhysics3D is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * JReactPhysics3D is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.jreactphysics3d.collision.shape;

import org.spout.jreactphysics3d.Configuration;
import org.spout.jreactphysics3d.mathematics.Matrix3x3;
import org.spout.jreactphysics3d.mathematics.Vector3;

/**
 * Represents a 3D box shape. Those axis are unit length. The three extents are half-widths of the
 * box along the three axis x, y, z local axis. The "transform" of the corresponding rigid body
 * gives an orientation and a position to the box.
 */
public class BoxShape extends CollisionShape {
	private final Vector3 mExtent = new Vector3();

	/**
	 * Constructs a box shape from its extents which is the vector between the two opposing corners
	 * that are the furthest away.
	 * @param extent The extent vector
	 */
	public BoxShape(Vector3 extent) {
		super(CollisionShape.CollisionShapeType.BOX);
		mExtent.set(extent);
	}

	/**
	 * Gets the extent vector, which is the vector between the two opposing corners that are the
	 * furthest away.
	 * @return The extents vector
	 */
	public Vector3 getExtent() {
		return mExtent;
	}

	/**
	 * Sets the extent vector, which is the vector between the two opposing corners that are the
	 * furthest away.
	 * @param extent The extents vector
	 */
	public void setExtent(Vector3 extent) {
		mExtent.set(extent);
	}

	@Override
	public Vector3 getLocalSupportPointWithMargin(Vector3 direction) {
		final float margin = getMargin();
		if (margin < 0) {
			throw new IllegalStateException("margin must be greater than zero");
		}
		return new Vector3(
				direction.getX() < 0 ? -mExtent.getX() - margin : mExtent.getX() + margin,
				direction.getY() < 0 ? -mExtent.getY() - margin : mExtent.getY() + margin,
				direction.getZ() < 0 ? -mExtent.getZ() - margin : mExtent.getZ() + margin);
	}

	@Override
	public Vector3 getLocalSupportPointWithoutMargin(Vector3 direction) {
		return new Vector3(
				direction.getX() < 0 ? -mExtent.getX() : mExtent.getX(),
				direction.getY() < 0 ? -mExtent.getY() : mExtent.getY(),
				direction.getZ() < 0 ? -mExtent.getZ() : mExtent.getZ());
	}

	@Override
	public Vector3 getLocalExtents(float margin) {
		return Vector3.add(mExtent, new Vector3(getMargin(), getMargin(), getMargin()));
	}

	@Override
	public float getMargin() {
		return Configuration.OBJECT_MARGIN;
	}

	@Override
	public void computeLocalInertiaTensor(Matrix3x3 tensor, float mass) {
		final float factor = (1f / 3) * mass;
		final float xSquare = mExtent.getX() * mExtent.getX();
		final float ySquare = mExtent.getY() * mExtent.getY();
		final float zSquare = mExtent.getZ() * mExtent.getZ();
		tensor.setAllValues(
				factor * (ySquare + zSquare), 0, 0,
				0, factor * (xSquare + zSquare), 0,
				0, 0, factor * (xSquare + ySquare));
	}
}
