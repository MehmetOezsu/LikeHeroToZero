package types;

import java.io.Serializable;

public enum Role implements Serializable {
	Publisher {
		@Override
		public String toString() {
			return "Publisher";
		}
	},
	Researcher {
		@Override
		public String toString() {
			return "Researcher";
		}
	}
}
