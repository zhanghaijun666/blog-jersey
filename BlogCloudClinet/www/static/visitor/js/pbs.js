/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
(function(global, factory) { /* global define, require, module */

    /* AMD */ if (typeof define === 'function' && define.amd)
        define(["protobufjs/light"], factory);

    /* CommonJS */ else if (typeof require === 'function' && typeof module === 'object' && module && module.exports)
        module.exports = factory(require("protobufjs/light"));

})(this, function($protobuf) {
    "use strict";

    var $root = ($protobuf.roots.pbStore || ($protobuf.roots.pbStore = new $protobuf.Root()))
    .setOptions({
      java_package: "com.proto",
      java_outer_classname: "PBStore"
    })
    .addJSON({
      Status: {
        values: {
          StatusDefault: 0,
          StatusActive: 1,
          StatusDeleted: 2
        }
      },
      Authenticator: {
        values: {
          DEFAULT_AUTHENTICATOR: 0,
          SYSTEM_AUTHENTICATOR: 1,
          TEXT_AUTHENTICATOR: 2,
          EMAIL_AUTHENTICATOR: 3
        }
      },
      ReturnCode: {
        values: {
          UNKNOWN_RETURN_CODE: 0,
          OK: 1,
          ERROR: 2,
          USER_EXIST: 50,
          PASSWORD_ERROR: 51,
          USERNAME_OR_PASSWORD_IS_EMPTY: 52,
          USER_EMPTY: 53,
          NOT_YOURSELF: 54
        }
      },
      User: {
        fields: {
          username: {
            type: "string",
            id: 1
          },
          nickname: {
            type: "string",
            id: 2
          },
          password: {
            type: "string",
            id: 3
          },
          role: {
            type: "Role",
            id: 4
          },
          email: {
            type: "string",
            id: 5
          },
          phone: {
            type: "string",
            id: 6
          },
          status: {
            type: "int32",
            id: 7
          },
          userId: {
            type: "int32",
            id: 8
          },
          authenticator: {
            type: "int32",
            id: 9
          }
        }
      },
      Role: {
        fields: {
          id: {
            type: "int32",
            id: 1
          },
          roleName: {
            type: "string",
            id: 2
          },
          note: {
            type: "string",
            id: 3
          },
          status: {
            type: "int32",
            id: 4
          },
          icon: {
            type: "string",
            id: 5
          }
        }
      },
      UserList: {
        fields: {
          items: {
            rule: "repeated",
            type: "User",
            id: 1
          }
        }
      },
      RoleList: {
        fields: {
          items: {
            rule: "repeated",
            type: "Role",
            id: 1
          }
        }
      },
      Menu: {
        fields: {
          menuId: {
            type: "int32",
            id: 1
          },
          parentId: {
            type: "int32",
            id: 2
          },
          name: {
            type: "string",
            id: 3
          },
          icon: {
            type: "string",
            id: 4
          },
          component: {
            type: "string",
            id: 5
          },
          hash: {
            type: "string",
            id: 6
          },
          deletable: {
            type: "bool",
            id: 7
          },
          defaultShow: {
            type: "bool",
            id: 8
          },
          status: {
            type: "int32",
            id: 9
          }
        }
      },
      MenuList: {
        fields: {
          items: {
            rule: "repeated",
            type: "Menu",
            id: 1
          }
        }
      },
      Protein: {
        fields: {
          id: {
            type: "string",
            id: 1
          },
          name: {
            type: "string",
            id: 2
          },
          family: {
            type: "string",
            id: 3
          },
          sort: {
            type: "string",
            id: 4
          },
          sources: {
            rule: "repeated",
            type: "string",
            id: 5
          },
          sequence: {
            type: "string",
            id: 6
          },
          secondary: {
            type: "string",
            id: 7
          },
          tertiary: {
            type: "string",
            id: 8
          },
          go: {
            type: "string",
            id: 9
          },
          kegg: {
            rule: "repeated",
            type: "string",
            id: 10
          },
          ppi: {
            type: "string",
            id: 11
          }
        }
      },
      ProteinList: {
        fields: {
          items: {
            rule: "repeated",
            type: "Protein",
            id: 1
          }
        }
      },
      RspInfo: {
        fields: {
          code: {
            type: "ReturnCode",
            id: 1
          },
          msg: {
            type: "string",
            id: 2
          }
        }
      },
      RspInfoList: {
        fields: {
          code: {
            type: "ReturnCode",
            id: 1
          },
          items: {
            rule: "repeated",
            type: "RspInfo",
            id: 2
          }
        }
      }
    });

    return $root;
});
