/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
(function(global, factory) { /* global define, require, module */

    /* AMD */ if (typeof define === 'function' && define.amd)
        define(["protobufjs/light"], factory);

    /* CommonJS */ else if (typeof require === 'function' && typeof module === 'object' && module && module.exports)
        module.exports = factory(require("protobufjs/light"));

})(this, function($protobuf) {
    "use strict";

    var $root = ($protobuf.roots.BlogStore || ($protobuf.roots.BlogStore = new $protobuf.Root()))
    .setOptions({
      java_package: "com.blog.config",
      java_outer_classname: "BlogStore"
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
      StoreTypeEnum: {
        values: {
          StoreTypeDefault: 0,
          StoreTypeCommit: 1,
          StoreTypeTree: 2,
          StoreTypeFile: 3
        }
      },
      GtypeEnum: {
        values: {
          Default: 0,
          User: 1,
          Group: 2
        }
      },
      ReturnCode: {
        values: {
          UNKNOWN_RETURN_CODE: 0,
          Return_OK: 1,
          Return_ERROR: 2,
          Return_USER_EXIST: 50,
          Return_PASSWORD_ERROR: 51,
          Return_USERNAME_OR_PASSWORD_IS_EMPTY: 52,
          Return_USER_EMPTY: 53,
          Return_NOT_YOURSELF: 54
        }
      },
      UserInfo: {
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
          },
          rememberMe: {
            type: "bool",
            id: 10
          }
        }
      },
      UserList: {
        fields: {
          items: {
            rule: "repeated",
            type: "UserInfo",
            id: 1
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
          template: {
            type: "string",
            id: 5
          },
          hash: {
            type: "string",
            id: 6
          },
          isDeletable: {
            type: "bool",
            id: 7
          },
          isDefaultShow: {
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
      },
      License: {
        fields: {
          licenseId: {
            type: "string",
            id: 1
          },
          company: {
            type: "string",
            id: 2
          },
          edition: {
            type: "string",
            id: 3
          },
          validUntil: {
            type: "int64",
            id: 4
          },
          productName: {
            type: "string",
            id: 5
          },
          shortProductName: {
            type: "string",
            id: 6
          },
          totalUser: {
            type: "int32",
            id: 7
          },
          totalVolume: {
            type: "int64",
            id: 8
          },
          value: {
            type: "string",
            id: 15
          },
          createByName: {
            type: "string",
            id: 16
          },
          createById: {
            type: "int32",
            id: 17
          },
          createAt: {
            type: "int64",
            id: 18
          }
        }
      },
      FileItemList: {
        fields: {
          item: {
            rule: "repeated",
            type: "FileItem",
            id: 1
          },
          parentFile: {
            type: "FileItem",
            id: 2
          }
        }
      },
      FileItem: {
        fields: {
          fileName: {
            type: "string",
            id: 1
          },
          contentType: {
            type: "string",
            id: 2
          },
          size: {
            type: "int64",
            id: 3
          },
          createTime: {
            type: "int64",
            id: 4
          },
          updateTime: {
            type: "int64",
            id: 5
          },
          fullPath: {
            type: "string",
            id: 6
          }
        }
      },
      Operator: {
        fields: {
          gptype: {
            type: "int32",
            id: 1
          },
          gpid: {
            type: "int32",
            id: 2
          }
        }
      },
      StorageItem: {
        fields: {
          type: {
            type: "StoreTypeEnum",
            id: 1
          },
          owner: {
            type: "Operator",
            id: 2
          },
          update: {
            type: "Operator",
            id: 3
          },
          createTime: {
            type: "int64",
            id: 4
          },
          updateTime: {
            type: "int64",
            id: 5
          },
          fileName: {
            type: "string",
            id: 6
          },
          size: {
            type: "int64",
            id: 7
          },
          contentType: {
            type: "string",
            id: 8
          },
          parent: {
            type: "string",
            id: 10
          },
          treeHashItem: {
            rule: "repeated",
            type: "string",
            id: 11
          },
          blobHashItem: {
            rule: "repeated",
            type: "string",
            id: 12
          }
        }
      },
      StoreBlob: {
        fields: {
          committer: {
            type: "Operator",
            id: 1
          },
          name: {
            type: "string",
            id: 2
          },
          contentType: {
            type: "string",
            id: 3
          },
          size: {
            type: "int64",
            id: 4
          },
          createTime: {
            type: "int64",
            id: 6
          }
        }
      },
      TreeUpdateItem: {
        fields: {
          originPath: {
            type: "string",
            id: 5
          },
          fileName: {
            type: "string",
            id: 6
          },
          content: {
            type: "bytes",
            id: 7
          },
          size: {
            type: "int64",
            id: 8
          }
        }
      },
      TreeUpdateItemList: {
        fields: {
          item: {
            rule: "repeated",
            type: "TreeUpdateItem",
            id: 4
          }
        }
      }
    });

    return $root;
});
