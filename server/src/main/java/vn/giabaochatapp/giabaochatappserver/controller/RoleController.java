package vn.giabaochatapp.giabaochatappserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.giabaochatapp.giabaochatappserver.data.domains.Role;
import vn.giabaochatapp.giabaochatappserver.data.dto.mix.PermissionKeyDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.mix.RoleIdDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.mix.RoleNameDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.mix.RoleRequestDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.response.StandardResponse;
import vn.giabaochatapp.giabaochatappserver.data.dto.shortName.RoleDTO;
import vn.giabaochatapp.giabaochatappserver.services.PermissionService;
import vn.giabaochatapp.giabaochatappserver.services.RoleService;
import vn.giabaochatapp.giabaochatappserver.services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/role")
@PreAuthorize("hasAuthority('ADMINISTRATOR')")
public class RoleController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/allRoles")
    public ResponseEntity<List<RoleDTO>> getRolePresentationList() {
        Iterable<Role> roleList = roleService.getRoleList();
        ArrayList<RoleDTO> list = new ArrayList<>();
        roleList.forEach(e -> list.add(new RoleDTO(e)));
        return ResponseEntity.ok(list);
    }

    @PostMapping("/create")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleRequestDTO roleRequest) {
        String role = roleRequest.getRole();
        return new ResponseEntity(new RoleDTO(roleService.createRole(role)), null, HttpStatus.CREATED);
    }

    @GetMapping("/getRoleByRoleId")
    public RoleDTO getRoleById(@RequestBody RoleIdDTO request) {
        return new RoleDTO(roleService.getRoleById(request.getRoleId()));
    }

    @DeleteMapping("/deleteRoleByRoleId")
    public StandardResponse deleteRoleById(@RequestBody RoleIdDTO request) {
        roleService.deleteRole(request.getRoleId());
        return StandardResponse.create("204", "Role deleted successfully", request.getRoleId());
    }

    @GetMapping("/findRoleByName")
    public List<RoleDTO> findRoleByName(@RequestBody RoleNameDTO request) {
        return roleService.findRoleByName(request.getRole());
    }

    @GetMapping("/findRoleByPermission")
    public List<RoleDTO> findRoleByPermission(@RequestBody PermissionKeyDTO request) {
        return roleService.findRoleByPermission(request.getPermissionKey());
    }

}
