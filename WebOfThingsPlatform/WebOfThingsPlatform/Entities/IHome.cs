using BrightstarDB.EntityFramework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebOfThingsPlatform
{
    [Entity]
    interface IHome
    {
        public Guid Id { get; set; }
        public string Name { get; set; }
        public List<Guid> UserIds { get; set; }
        public List<Guid> RoleIds { get; set; }
        public List<Guid> DeviceIds { get; set; }
    }
}
