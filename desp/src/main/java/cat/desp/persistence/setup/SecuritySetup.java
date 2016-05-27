package cat.desp.persistence.setup;

import java.util.Set;

import cat.desp.common.spring.util.Profiles;
import cat.desp.persistence.model.Principal;
import cat.desp.persistence.model.Privilege;
import cat.desp.persistence.model.Role;
import cat.desp.service.IPrincipalService;
import cat.desp.service.IPrivilegeService;
import cat.desp.service.IRoleService;
import cat.desp.util.Desp;
import cat.desp.util.Desp.Privileges;
import cat.desp.util.Desp.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

/**
 * This simple setup class will run during the bootstrap process of Spring and will create some setup data <br>
 * The main focus here is creating some standard privileges, then roles and finally some default principals/users
 */
@Component
@Profile(Profiles.DEPLOYED)
public class SecuritySetup implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(SecuritySetup.class);

    private boolean setupDone;

    @Autowired
    private IPrincipalService principalService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPrivilegeService privilegeService;

    public SecuritySetup() {
        super();
    }

    //

    /**
     * - note that this is a compromise - the flag makes this bean statefull which can (and will) be avoided in the future by a more advanced mechanism <br>
     * - the reason for this is that the context is refreshed more than once throughout the lifecycle of the deployable <br>
     * - alternatives: proper persisted versioning
     */
    @Override
    public final void onApplicationEvent(final ContextRefreshedEvent event) {
        if (!setupDone) {
            logger.info("Executing Setup");

            createPrivileges();
            createRoles();
            createPrincipals();

            setupDone = true;
            logger.info("Setup Done");
        }
    }

    // Privilege

    private void createPrivileges() {
        createPrivilegeIfNotExisting(Privileges.CAN_PRIVILEGE_READ);
        createPrivilegeIfNotExisting(Privileges.CAN_PRIVILEGE_WRITE);

        createPrivilegeIfNotExisting(Privileges.CAN_ROLE_READ);
        createPrivilegeIfNotExisting(Privileges.CAN_ROLE_WRITE);

        createPrivilegeIfNotExisting(Privileges.CAN_USER_READ);
        createPrivilegeIfNotExisting(Privileges.CAN_USER_WRITE);
    }

    final void createPrivilegeIfNotExisting(final String name) {
        createPrivilegeIfNotExisting(name, name);
    }

    final void createPrivilegeIfNotExisting(final String name, final String description) {
        final Privilege entityByName = privilegeService.findOneByName(name);
        if (entityByName == null) {
            final Privilege entity = new Privilege(name, description);
            privilegeService.create(entity);
        }
    }

    // Role

    private void createRoles() {
        final Privilege canPrivilegeRead = privilegeService.findOneByName(Privileges.CAN_PRIVILEGE_READ);
        final Privilege canPrivilegeWrite = privilegeService.findOneByName(Privileges.CAN_PRIVILEGE_WRITE);
        final Privilege canRoleRead = privilegeService.findOneByName(Privileges.CAN_ROLE_READ);
        final Privilege canRoleWrite = privilegeService.findOneByName(Privileges.CAN_ROLE_WRITE);
        final Privilege canUserRead = privilegeService.findOneByName(Privileges.CAN_USER_READ);
        final Privilege canUserWrite = privilegeService.findOneByName(Privileges.CAN_USER_WRITE);

        Preconditions.checkNotNull(canPrivilegeRead);
        Preconditions.checkNotNull(canPrivilegeWrite);
        Preconditions.checkNotNull(canRoleRead);
        Preconditions.checkNotNull(canRoleWrite);
        Preconditions.checkNotNull(canUserRead);
        Preconditions.checkNotNull(canUserWrite);

        createRoleIfNotExisting(Roles.ROLE_ADMIN, Sets.<Privilege> newHashSet(canUserRead, canUserWrite, canRoleRead, canRoleWrite, canPrivilegeRead, canPrivilegeWrite));
    }

    final void createRoleIfNotExisting(final String name, final Set<Privilege> privileges) {
        final Role entityByName = roleService.findOneByName(name);
        if (entityByName == null) {
            final Role entity = new Role(name);
            entity.setPrivileges(privileges);
            roleService.create(entity);
        }
    }

    // Principal/User

    final void createPrincipals() {
        final Role roleAdmin = roleService.findOneByName(Roles.ROLE_ADMIN);

        // createPrincipalIfNotExisting(SecurityConstants.ADMIN_USERNAME, SecurityConstants.ADMIN_PASS, Sets.<Role> newHashSet(roleAdmin));
        createPrincipalIfNotExisting(Desp.ADMIN_EMAIL, Desp.ADMIN_PASS, Sets.<Role> newHashSet(roleAdmin));
    }

    final void createPrincipalIfNotExisting(final String loginName, final String pass, final Set<Role> roles) {
        final Principal entityByName = principalService.findOneByName(loginName);
        if (entityByName == null) {
            final Principal entity = new Principal(loginName, pass, roles);
            principalService.create(entity);
        }
    }

}
