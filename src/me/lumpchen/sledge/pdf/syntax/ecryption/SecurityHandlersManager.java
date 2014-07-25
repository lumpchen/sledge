package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.lang.reflect.Constructor;
import java.util.Hashtable;

public class SecurityHandlersManager {

	private static SecurityHandlersManager instance;

	private Hashtable<String, Class<?>> handlerNames = null;

	private Hashtable<Class<?>, Class<?>> handlerPolicyClasses = null;

	private SecurityHandlersManager() {
		handlerNames = new Hashtable<String, Class<?>>();
		handlerPolicyClasses = new Hashtable<Class<?>, Class<?>>();
		try {
			this.registerHandler(StandardSecurityHandler.FILTER, StandardSecurityHandler.class,
					StandardProtectionPolicy.class);
			this.registerHandler(PublicKeySecurityHandler.FILTER, PublicKeySecurityHandler.class,
					PublicKeyProtectionPolicy.class);
		} catch (BadSecurityHandlerException e) {
			// should never happen
			throw new RuntimeException(e);
		}
	}

	public void registerHandler(String filterName, Class<?> securityHandlerClass,
			Class<?> protectionPolicyClass) throws BadSecurityHandlerException {
		if (handlerNames.contains(securityHandlerClass)
				|| handlerPolicyClasses.contains(securityHandlerClass)) {
			throw new BadSecurityHandlerException(
					"the following security handler was already registered: "
							+ securityHandlerClass.getName());
		}

		if (SecurityHandler.class.isAssignableFrom(securityHandlerClass)) {
			try {
				if (handlerNames.containsKey(filterName)) {
					throw new BadSecurityHandlerException(
							"a security handler was already registered " + "for the filter name "
									+ filterName);
				}
				if (handlerPolicyClasses.containsKey(protectionPolicyClass)) {
					throw new BadSecurityHandlerException(
							"a security handler was already registered " + "for the policy class "
									+ protectionPolicyClass.getName());
				}

				handlerNames.put(filterName, securityHandlerClass);
				handlerPolicyClasses.put(protectionPolicyClass, securityHandlerClass);
			} catch (Exception e) {
				throw new BadSecurityHandlerException(e);
			}
		} else {
			throw new BadSecurityHandlerException(
					"The class is not a super class of SecurityHandler");
		}
	}

	public static SecurityHandlersManager getInstance() {
		if (instance == null) {
			instance = new SecurityHandlersManager();
			// Security.addProvider(new BouncyCastleProvider());
		}
		return instance;
	}

	public SecurityHandler getSecurityHandler(ProtectionPolicy policy)
			throws BadSecurityHandlerException {

		Object found = handlerPolicyClasses.get(policy.getClass());
		if (found == null) {
			throw new BadSecurityHandlerException(
					"Cannot find an appropriate security handler for "
							+ policy.getClass().getName());
		}
		Class<?> handlerclass = (Class<?>) found;
		Class<?>[] argsClasses = { policy.getClass() };
		Object[] args = { policy };
		try {
			Constructor<?> c = handlerclass.getDeclaredConstructor(argsClasses);
			SecurityHandler handler = (SecurityHandler) c.newInstance(args);
			return handler;
		} catch (Exception e) {
			throw new BadSecurityHandlerException(
					"problem while trying to instanciate the security handler "
							+ handlerclass.getName() + ": " + e.getMessage());
		}
	}

	public SecurityHandler getSecurityHandler(String filterName) throws BadSecurityHandlerException {
		Object found = handlerNames.get(filterName);
		if (found == null) {
			throw new BadSecurityHandlerException(
					"Cannot find an appropriate security handler for " + filterName);
		}
		Class<?> handlerclass = (Class<?>) found;
		Class<?>[] argsClasses = {};
		Object[] args = {};
		try {
			Constructor<?> c = handlerclass.getDeclaredConstructor(argsClasses);
			SecurityHandler handler = (SecurityHandler) c.newInstance(args);
			return handler;
		} catch (Exception e) {
			throw new BadSecurityHandlerException(
					"problem while trying to instanciate the security handler "
							+ handlerclass.getName() + ": " + e.getMessage());
		}
	}
}
