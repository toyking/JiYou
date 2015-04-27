using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using JiFriend.Models;

namespace JiFriend.Controllers
{
    public class SquarePrizesController : Controller
    {
        private JiFriendContext db = new JiFriendContext();

        // GET: SquarePrizes
        public ActionResult Index()
        {
            return View(db.SquarePrizes.ToList());
        }

        // GET: SquarePrizes/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            SquarePrize squarePrize = db.SquarePrizes.Find(id);
            if (squarePrize == null)
            {
                return HttpNotFound();
            }
            return View(squarePrize);
        }

        // GET: SquarePrizes/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: SquarePrizes/Create
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "id,square_id,time,username")] SquarePrize squarePrize)
        {
            if (ModelState.IsValid)
            {
                db.SquarePrizes.Add(squarePrize);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(squarePrize);
        }

        // GET: SquarePrizes/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            SquarePrize squarePrize = db.SquarePrizes.Find(id);
            if (squarePrize == null)
            {
                return HttpNotFound();
            }
            return View(squarePrize);
        }

        // POST: SquarePrizes/Edit/5
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "id,square_id,time,username")] SquarePrize squarePrize)
        {
            if (ModelState.IsValid)
            {
                db.Entry(squarePrize).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(squarePrize);
        }

        // GET: SquarePrizes/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            SquarePrize squarePrize = db.SquarePrizes.Find(id);
            if (squarePrize == null)
            {
                return HttpNotFound();
            }
            return View(squarePrize);
        }

        // POST: SquarePrizes/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            SquarePrize squarePrize = db.SquarePrizes.Find(id);
            db.SquarePrizes.Remove(squarePrize);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
