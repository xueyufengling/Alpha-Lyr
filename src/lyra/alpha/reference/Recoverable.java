package lyra.alpha.reference;

public interface Recoverable<Derived> {
	@SuppressWarnings("unchecked")
	public default Derived redirect() {
		return (Derived) this;
	}

	@SuppressWarnings("unchecked")
	public default Derived recovery() {
		return (Derived) this;
	}

	@SuppressWarnings("unchecked")
	public default Derived asPrimary() {
		return (Derived) this;
	}
}
