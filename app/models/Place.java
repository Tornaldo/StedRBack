/*
 *	Copyright (c) 2013, Odd Fredrik Rogstad, Christian Frøystad, Simon Stastny, Knut Nergård
 *	All rights reserved.
 *	
 *	Redistribution and use in source and binary forms, with or without
 *	modification, are permitted provided that the following conditions are met:
 *	* Redistributions of source code must retain the above copyright
 *	  notice, this list of conditions and the following disclaimer.
 *	* Redistributions in binary form must reproduce the above copyright
 *	  notice, this list of conditions and the following disclaimer in the
 *	  documentation and/or other materials provided with the distribution.
 *	* Neither the name of the project nor the 
 *	  names of its contributors may be used to endorse or promote products
 *	  derived from this software without specific prior written permission.
 *	
 *	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNERS BE LIABLE FOR ANY
 *	DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package models;

import java.util.Date;

import com.google.common.base.Predicate;

public class Place {

	public String id;

	public String owner;

	public String ownerName;

	public String title;

	public Double latitude;

	public Double longitude;

	public Date dateAdded;

	public String pictureUrl;

	public String thumbnailUrl;
	
	public Integer license;
	
	public boolean hasCompatibleLicense() {
		// picture does not have coordinates
		if (this.license == null) {
			return false;
		}

		if (this.license == 0) {
			return false;
		}

		/*
		 * 0 All Rights Reserved
		 * 1 Attribution-NonCommercial-ShareAlike License
		 * 2 Attribution-NonCommercial License 
		 * 3 Attribution-NonCommercial-NoDerivs License 
		 * 4 Attribution License 
		 * 5 Attribution-ShareAlike License 
		 * 6 Attribution-NoDerivs License 
		 * 7 No known copyright restrictions 
		 * 8 United States Government Work
		 */

		return true;
	}
	
	public boolean hasLocation() {
		// picture does not have coordinates
		if (this.latitude == null || this.longitude == null) {
			return false;
		}
		
		return true;
	}

	public static class HasId implements Predicate<Place> { 
		private final String id;
		
		public HasId(String id) {
			super();
			this.id = id;
		}

		@Override
		public boolean apply(Place place) {
			return place.id.equals(id);
		}
	};

	public static class HasLocation implements Predicate<Place> { 
		@Override
		public boolean apply(Place place) {
			return place.hasLocation();
		}

	};

	public static class IsInArea implements Predicate<Place> {
		private final Double latBL;
		private final Double latTR;
		private final Double lngBL;
		private final Double lngTR;

		public IsInArea(Double latBL, Double latTR, Double lngBL, Double lngTR) {
			super();
			this.latBL = latBL;
			this.latTR = latTR;
			this.lngBL = lngBL;
			this.lngTR = lngTR;
		}

		@Override
		public boolean apply(Place place) {
			// picture does not have coordinates
			if (place == null || place.latitude == null || place.longitude == null) {
				return false;
			}

			if (place.latitude < latBL || place.latitude > latTR) {
				return false;
			}

			if (place.longitude < lngBL || place.longitude > lngTR) {
				return false;
			}

			return true;
		}
	};
	
	public static class HasCompatibleLicense implements Predicate<Place> {
		@Override
		public boolean apply(Place place) {
			return place.hasCompatibleLicense();
		}
	}
	
	@Override
	public String toString() {
		return "id: " + id + ", license: " + license + ", lat: " + latitude + ", long: " + longitude;
	}

}
